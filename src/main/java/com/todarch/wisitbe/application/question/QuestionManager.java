package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.application.staticdata.StaticDataManager;
import com.todarch.wisitbe.domain.fix.City;
import com.todarch.wisitbe.domain.fix.CityRepository;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.rest.game.SimpleQuestion;
import com.todarch.wisitbe.rest.question.AnswerQuestion;
import com.todarch.wisitbe.rest.question.NewQuestionReq;
import com.todarch.wisitbe.rest.question.QuestionAnswer;
import com.todarch.wisitbe.rest.question.QuestionWithNoAnswer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionManager {

  private final CityRepository cityRepository;

  private final QuestionRepository questionRepository;

  private final StaticDataManager staticDataManager;

  private final PictureManager pictureManager;

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
    Objects.requireNonNull(newQuestionReq.getCity(), "city cannot be null");
    Picture picture = new Picture();
    picture.setUrl(newQuestionReq.getPicUrl());
    Long cityId = staticDataManager.tryToFindCityId(newQuestionReq.getCity());
    picture.setCityId(cityId);

    return new Question(UUID.randomUUID(), picture, staticDataManager.prepareChoices(cityId));
  }

  public QuestionWithNoAnswer nextFor(String userId) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    List<Question> questions = questionRepository.findAll();
    Question question = questions.get(random.nextInt(questions.size()));
    QuestionWithNoAnswer questionWithNoAnswer = new QuestionWithNoAnswer();
    questionWithNoAnswer.setQuestionId(question.getId());
    questionWithNoAnswer.setPicUrl(question.pictureUrl());
    questionWithNoAnswer.setChoices(staticDataManager.toCityNames(question.choices()));
    questionWithNoAnswer.setChoiceCityIds(question.choices());
    return questionWithNoAnswer;
  }

  public QuestionAnswer answer(AnswerQuestion answerQuestion) {
    Objects.requireNonNull(answerQuestion.getQuestionId(), "QuestionId is required");
    Question question = questionRepository.findById(answerQuestion.getQuestionId())
        .orElseThrow(() -> new RuntimeException("Question not found: " + answerQuestion.getQuestionId()));

    boolean knew = question.isCorrectAnswer(answerQuestion.getCityId());

    QuestionAnswer questionAnswer = new QuestionAnswer();
    questionAnswer.setQuestionId(answerQuestion.getQuestionId());
    questionAnswer.setCorrectCity(staticDataManager.getCityById(question.answerCityId()));
    questionAnswer.setGivenCity(staticDataManager.getCityById(answerQuestion.getCityId()));
    questionAnswer.setKnew(knew);

    return questionAnswer;
  }
}
