package com.todarch.wisitbe.domain.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.todarch.wisitbe.data.QuestionData;
import org.junit.jupiter.api.Test;

class UserQuestionTest {

  @Test
  void defaultPriorityMeansNewQuestion() {
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setWeight(UserQuestion.DEFAULT_WEIGHT);
    assertThat(userQuestion.isNew()).isTrue();
  }

  @Test
  void isNotANewQuestionIfAnsweredWrong() {
    Question question = QuestionData.newQuestion();
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    boolean knew = userQuestion.answer(6);

    assertThat(knew).isFalse();
    assertThat(userQuestion.isNew()).isFalse();
  }

  @Test
  void isNotANewQuestionIfAnsweredCorrect() {
    Question question = QuestionData.newQuestion();
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    boolean knew = userQuestion.answer(userQuestion.getQuestion().answerCityId());

    assertThat(knew).isTrue();
    assertThat(userQuestion.isNew()).isFalse();
  }

  @Test
  void canBeAskedAgainAndAgainIfAnsweredWrong() {
    Question question = QuestionData.newQuestion();
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    long correctAnswer = userQuestion.getQuestion().answerCityId();
    long wrongAnswer = correctAnswer - 1;

    for (int i = 0; i < 50; i++) {
      userQuestion.answer(wrongAnswer);
    }

    assertThat(userQuestion.canBeAskedAgain()).isTrue();
  }

  @Test
  void cannotBeAskedAgainIfAnsweredCorrectly5Times() {
    Question question = QuestionData.newQuestion();
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    long correctAnswer = userQuestion.getQuestion().answerCityId();

    for (int i = 0; i < 5; i++) {
      userQuestion.answer(correctAnswer);
    }

    assertThat(userQuestion.canBeAskedAgain()).isFalse();
  }

  @Test
  void answeringWrongBetweenAnsweringCorrectShouldNotAffectAskingCount() {
    Question question = QuestionData.newQuestion();
    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    long correctAnswer = userQuestion.getQuestion().answerCityId();
    long wrongAnswer = correctAnswer + 1;

    userQuestion.answer(correctAnswer);
    userQuestion.answer(correctAnswer);
    userQuestion.answer(wrongAnswer);
    userQuestion.answer(wrongAnswer);
    userQuestion.answer(wrongAnswer);
    userQuestion.answer(correctAnswer);
    userQuestion.answer(correctAnswer);
    userQuestion.answer(correctAnswer);

    assertThat(userQuestion.canBeAskedAgain()).isFalse();
  }

  @Test
  void tellsCorrectAnswer() {
    Question question = QuestionData.newQuestion();
    long expectedAnswer = question.answerCityId();

    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    long correctAnswer = userQuestion.correctAnswer();

    assertThat(correctAnswer).isEqualTo(expectedAnswer);
  }

  @Test
  void newQuestionDoesNotHaveLastAskedAt() {
    UserQuestion userQuestion = new UserQuestion();
    assertThat(userQuestion.lastAskedAt()).isEmpty();
  }

  @Test
  void lastAskedAtGetsUpdatedWhenAnswered() {
    Question question = QuestionData.newQuestion();

    UserQuestion userQuestion = new UserQuestion();
    userQuestion.setQuestion(question);

    userQuestion.answer(5L);

    assertThat(userQuestion.lastAskedAt()).isNotEmpty();
  }

}
