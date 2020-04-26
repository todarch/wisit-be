package com.todarch.wisitbe.application.question;

import static com.todarch.wisitbe.application.question.UserQuestionManager.NEW_QUESTION_PICKING_LIMIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.data.QuestionData;
import com.todarch.wisitbe.data.UserData;
import com.todarch.wisitbe.data.UserQuestionData;
import com.todarch.wisitbe.domain.question.Question;
import com.todarch.wisitbe.domain.question.QuestionRepository;
import com.todarch.wisitbe.domain.question.UserQuestion;
import com.todarch.wisitbe.domain.question.UserQuestionPickingSpecification;
import com.todarch.wisitbe.domain.question.UserQuestionRepository;
import com.todarch.wisitbe.domain.user.User;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.AlmostAllUserQuestionsAskedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.provider.TimeProvider;
import com.todarch.wisitbe.rest.question.AnswerUserQuestion;
import com.todarch.wisitbe.rest.question.PreparedUserQuestion;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserQuestionManagerTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private QuestionRepository questionRepository;

  @Mock
  private UserQuestionRepository userQuestionRepository;

  @Mock
  private TimeProvider timeProvider;

  @Mock
  private WisitEventPublisher wisitEventPublisher;

  @Mock
  private LocationManager locationManager;

  @Mock
  private QuestionManager questionManager;

  @Mock
  private UserQuestionPickingSpecification userQuestionPickingSpecification;

  @InjectMocks
  private UserQuestionManager userQuestionManager;

  private LocalDateTime fixedTimeAsNow;

  @BeforeEach
  void setUp() {
    fixedTimeAsNow = LocalDateTime.of(LocalDate.ofYearDay(1992, 2), LocalTime.now());
  }

  @Nested
  class PickForTests {

    @Test
    void pivotDateBecomesOldestPickedQuestionCreatedAtWhenThereAreEnoughNewQuestions() {
      User testUser = UserData.newUser();
      doReturn(testUser).when(userRepository).getById(testUser.getId());

      LocalDateTime currentPivotPoint = testUser.pivotPoint();

      final int greaterThanNewQuestionLimit = NEW_QUESTION_PICKING_LIMIT + 5;
      List<Question> questionsAfterPivotDate =
          QuestionData.questions(greaterThanNewQuestionLimit);

      int lastQuestionIndex = greaterThanNewQuestionLimit - 1;
      final LocalDateTime oldestCreatedAt =
          questionsAfterPivotDate.get(lastQuestionIndex).createdAt();

      doReturn(questionsAfterPivotDate)
          .when(questionRepository).pick20QuestionsCreatedAfter(currentPivotPoint);

      userQuestionManager.pickFor(testUser.id());

      assertThat(testUser.pivotPoint()).isNotEqualTo(currentPivotPoint);
      assertThat(testUser.pivotPoint()).isSameAs(oldestCreatedAt);

      verify(userRepository).save(testUser);

      verifySizeOfSavedNewUserQuestion(greaterThanNewQuestionLimit);
    }

    private void verifySizeOfSavedNewUserQuestion(int size) {
      ArgumentCaptor<List<UserQuestion>> captor = ArgumentCaptor.forClass(List.class);
      verify(userQuestionRepository).saveAll(captor.capture());

      List<UserQuestion> newUserQuestions = captor.getValue();
      assertThat(newUserQuestions).hasSize(size);
    }

    @Test
    void pivotDatesBecomesNowWhenThereAreNotEnoughNewQuestions() {
      User testUser = UserData.newUser();
      doReturn(testUser).when(userRepository).getById(testUser.getId());

      LocalDateTime currentPivotPoint = testUser.pivotPoint();

      final int lowerThanEnoughQuestionThreshold = NEW_QUESTION_PICKING_LIMIT - 10;
      List<Question> questionsAfterPivotDate =
          QuestionData.questions(lowerThanEnoughQuestionThreshold);

      doReturn(questionsAfterPivotDate)
          .when(questionRepository).pick20QuestionsCreatedAfter(currentPivotPoint);

      doReturn(fixedTimeAsNow).when(timeProvider).now();

      userQuestionManager.pickFor(testUser.getId());

      assertThat(testUser.pivotPoint()).isEqualTo(fixedTimeAsNow);

      verify(userRepository).save(testUser);
      verifySizeOfSavedNewUserQuestion(lowerThanEnoughQuestionThreshold);
    }

    @Test
    void pivotDateBecomesNowWhenThereIsNoNewQuestion() {
      User testUser = UserData.newUser();
      doReturn(testUser).when(userRepository).getById(testUser.getId());

      LocalDateTime currentPivotPoint = testUser.pivotPoint();

      doReturn(Collections.emptyList())
          .when(questionRepository).pick20QuestionsCreatedAfter(currentPivotPoint);

      doReturn(fixedTimeAsNow).when(timeProvider).now();

      userQuestionManager.pickFor(testUser.getId());

      assertThat(testUser.pivotPoint()).isEqualTo(fixedTimeAsNow);

      verify(userRepository).save(testUser);
      verifySizeOfSavedNewUserQuestion(0);
    }

  }

  @Nested
  class NextForTests {

    @Test
    void publishesEventForNewQuestionIfPickingSpecificationIsSatisfied() {
      User testUser = UserData.newUser();
      doReturn(testUser).when(userRepository).getById(testUser.getId());

      doReturn(true)
          .when(userQuestionPickingSpecification).isSatisfiedBy(testUser, List.of());

      doReturn(Optional.empty()).when(userQuestionRepository).nextFor(testUser.id());

      Optional<PreparedUserQuestion> preparedUserQuestion =
          userQuestionManager.nextFor(testUser.id());

      assertThat(preparedUserQuestion).isEmpty();

      verify(userQuestionPickingSpecification).isSatisfiedBy(testUser, List.of());
      verifyEventPublished(testUser);
    }
  }

  private void verifyEventPublished(User testUser) {
    ArgumentCaptor<AlmostAllUserQuestionsAskedEvent> captor =
        ArgumentCaptor.forClass(AlmostAllUserQuestionsAskedEvent.class);

    verify(wisitEventPublisher).publishEvent(captor.capture());

    AlmostAllUserQuestionsAskedEvent publishedEvent = captor.getValue();

    assertThat(publishedEvent.getUserId()).isEqualTo(testUser.id());
  }

  @Nested
  class NextBatchForTests {

    @Test
    void publishesEventForNewQuestionIfPickingSpecificationIsSatisfied() {
      User testUser = UserData.newUser();
      doReturn(testUser).when(userRepository).getById(testUser.getId());

      doReturn(true)
          .when(userQuestionPickingSpecification).isSatisfiedBy(testUser, List.of());

      doReturn(List.of()).when(userQuestionRepository).next10(testUser.id());

      userQuestionManager.nextBatchFor(testUser.id());

      verifyEventPublished(testUser);
    }

    @Test
    void preparesAtLeast10NextQuestionsForUser() {
      User testUser = UserData.newUser();
      doReturn(testUser).when(userRepository).getById(testUser.getId());

      UserQuestion userQuestion = UserQuestionData.newUserQuestionFor(testUser);
      doReturn(List.of(userQuestion)).when(userQuestionRepository).next10(testUser.id());

      List<PreparedUserQuestion> preparedUserQuestions =
          userQuestionManager.nextBatchFor(testUser.id());

      assertThat(preparedUserQuestions).hasSize(1);
    }
  }

  @Nested
  class AnswerTests {

    @Test
    void userQuestionIdIsRequired() {
      User testUser = UserData.newUser();

      AnswerUserQuestion answerUserQuestion = new AnswerUserQuestion();

      assertThatNullPointerException()
          .isThrownBy(() -> {
            userQuestionManager.answer(testUser.id(), answerUserQuestion);
          })
          .withMessageContaining("required");
    }

    @Test
    void updatesUserQuestionAfterBeingAnswering() {
      User testUser = UserData.newUser();
      UserQuestion userQuestion = UserQuestionData.newUserQuestionFor(testUser);

      AnswerUserQuestion answerUserQuestion = new AnswerUserQuestion();
      answerUserQuestion.setUserQuestionId(userQuestion.getId());
      answerUserQuestion.setCityId(5L);

      doReturn(userQuestion)
          .when(userQuestionRepository).getByIdAndUserId(userQuestion.getId(), testUser.id());

      userQuestionManager.answer(testUser.id(), answerUserQuestion);

      verify(userQuestionRepository).save(userQuestion);
    }

  }
}
