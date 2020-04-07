package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.application.staticdata.StaticDataManager;
import com.todarch.wisitbe.domain.fix.City;
import com.todarch.wisitbe.domain.fix.CityRepository;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.QuestionCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.rest.game.SimpleQuestion;
import com.todarch.wisitbe.rest.picture.NewPictureReq;
import com.todarch.wisitbe.rest.question.AnswerUserQuestion;
import com.todarch.wisitbe.rest.question.NewQuestionReq;
import com.todarch.wisitbe.rest.question.PreparedQuestion;
import com.todarch.wisitbe.rest.question.UserQuestionAnswer;
import com.todarch.wisitbe.rest.question.PreparedUserQuestion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionManager {

  private final CityRepository cityRepository;

  private final QuestionRepository questionRepository;

  private final StaticDataManager staticDataManager;

  private final PictureManager pictureManager;

  private final WisitEventPublisher wisitEventPublisher;

  private final UserQuestionRepository userQuestionRepository;

  private Map<Long, String> citiesById;

  private List<Long> cityIds;

  @PostConstruct
  private void loadData() {
    citiesById = cityRepository.findAll()
        .stream()
        .collect(Collectors.toUnmodifiableMap(City::getId, City::getName));

    cityIds = new ArrayList<>(citiesById.keySet());
  }

  public SimpleQuestion next() {

    Picture next = pictureManager.next();

    SimpleQuestion simpleQuestion = new SimpleQuestion();
    simpleQuestion.setPicUrl(next.getUrl());
    simpleQuestion.setAnswer(citiesById.get(next.getCityId()));
    simpleQuestion.setChoices(prepareChoices(next.getCityId()));

    return simpleQuestion;
  }

  private List<String> prepareChoices(long answerCityId) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Set<String> choices = new HashSet<>(4);
    choices.add(citiesById.get(answerCityId));

    while (choices.size() != 4) {
      int randomIndex = random.nextInt(cityIds.size());
      Long randomId = cityIds.get(randomIndex);
      choices.add(citiesById.get(randomId));
    }

    return new ArrayList<>(choices);
  }

  public Question createQuestion(NewQuestionReq newQuestionReq) {
    Objects.requireNonNull(newQuestionReq.getPicUrl(), "picurl cannot be null");
    Objects.requireNonNull(newQuestionReq.getCityName(), "city cannot be null");

    NewPictureReq newPictureReq = new NewPictureReq();
    newPictureReq.setPicUrl(newQuestionReq.getPicUrl());
    newPictureReq.setCityName(newQuestionReq.getCityName());
    Picture savedPicture = pictureManager.newPicture(newPictureReq);

    Set<Long> choices = staticDataManager.prepareChoices(savedPicture.getCityId());
    Question newQuestion = new Question(UUID.randomUUID(), savedPicture, choices);
    Question savedQuestion = questionRepository.save(newQuestion);

    QuestionCreatedEvent questionCreatedEvent = new QuestionCreatedEvent();
    questionCreatedEvent.setQuestionId(savedQuestion.getId());
    wisitEventPublisher.publishEvent(questionCreatedEvent);

    return savedQuestion;
  }

  public Optional<PreparedUserQuestion> nextFor(String userId) {
    List<com.todarch.wisitbe.domain.question.UserQuestion> allQuestionsForUser = userQuestionRepository.findAllByUserId(userId);
    if (allQuestionsForUser.isEmpty()) {
      return Optional.empty();
    }

    int randomIndex = randomIndex(allQuestionsForUser.size());
    com.todarch.wisitbe.domain.question.UserQuestion userQuestion = allQuestionsForUser.get(randomIndex);

    return Optional.of(toQuestionWithNoAnswer(userQuestion));
  }

  private int randomIndex(int bound) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    return random.nextInt(bound);
  }

  private PreparedUserQuestion toQuestionWithNoAnswer(UserQuestion userQuestion) {
    Question question = userQuestion.getQuestion();
    PreparedUserQuestion preparedUserQuestion = new PreparedUserQuestion();
    preparedUserQuestion.setUserQuestionId(userQuestion.getId());
    preparedUserQuestion.setPicUrl(question.pictureUrl());
    preparedUserQuestion.setChoices(staticDataManager.toCityNames(question.choices()));
    preparedUserQuestion.setChoiceCityIds(question.choices());
    return preparedUserQuestion;
  }

  public UserQuestionAnswer answer(@NonNull String userId, @NonNull AnswerUserQuestion answerUserQuestion) {
    String userQuestionId = answerUserQuestion.getUserQuestionId();
    Objects.requireNonNull(userQuestionId, "UserQuestionId is required");

    UserQuestion userQuestion = userQuestionRepository.findById(userQuestionId)
        .orElseThrow(() -> new RuntimeException("UserQuestion not found: " + userQuestionId));

    if (!userQuestion.getUserId().equals(userId)) {
        throw new RuntimeException(userQuestionId + " user question is not for user: " + userId);
    }

    Question question = userQuestion.getQuestion();

    boolean knew = question.isCorrectAnswer(answerUserQuestion.getCityId());

    UserQuestionAnswer userQuestionAnswer = new UserQuestionAnswer();
    userQuestionAnswer.setUserQuestionId(userQuestion.getId());
    userQuestionAnswer.setCorrectCity(staticDataManager.getCityById(question.answerCityId()));
    userQuestionAnswer.setGivenCity(staticDataManager.getCityById(answerUserQuestion.getCityId()));
    userQuestionAnswer.setKnew(knew);

    return userQuestionAnswer;
  }

  public Optional<PreparedQuestion> getById(String questionId) {
    return questionRepository.findById(questionId)
        .map(this::toQuestionWithNoAnswer);
  }

  private PreparedQuestion toQuestionWithNoAnswer(Question question) {
    PreparedQuestion preparedQuestion = new PreparedQuestion();
    preparedQuestion.setQuestionId(question.getId());
    preparedQuestion.setPicUrl(question.pictureUrl());
    preparedQuestion.setChoices(staticDataManager.toCityNames(question.choices()));
    preparedQuestion.setChoiceCityIds(question.choices());
    return preparedQuestion;
  }
}
