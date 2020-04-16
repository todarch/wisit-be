package com.todarch.wisitbe.domain.question;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.question.Question;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class QuestionTest {

  private static final java.util.UUID TEST_ID = UUID.randomUUID();

  @Test
  void canCreateQuestion() {
    Question question = validQuestion();
    assertThat(question).isNotNull();
  }

  @Test
  void cannotHaveLessThan3Choices() {
    Picture picture = new Picture();
    Set<Long> twoChoices = Set.of(1L, 2L);

    try {
      new Question(TEST_ID, picture, twoChoices);
      fail("Should have thrown exception cus of insufficient choices");
    } catch (IllegalArgumentException ex) {
      assertThat(ex).hasMessageContaining("Insufficient choices");
    }
  }

  @Test
  void cannotHaveMoreThan4Choices() {
    Picture picture = new Picture();
    Set<Long> fiveChoices = Set.of(1L, 2L, 3L, 4L, 5L);

    try {
      new Question(TEST_ID, picture, fiveChoices);
      fail("Should have thrown exception cus of too many choices");
    } catch (IllegalArgumentException ex) {
      assertThat(ex).hasMessageContaining("Too many choices");
    }
  }

  @Test
  void picturesCityCannotBeOneOfChoices() {
    Picture picture = new Picture();
    long cityId = 2L;
    picture.setCityId(cityId);

    Set<Long> threeChoices = Set.of(1L, cityId, 3L);

    try {
      new Question(TEST_ID, picture, threeChoices);
      fail("City of picture cannot be in choices");
    } catch (IllegalArgumentException ex) {
      assertThat(ex).hasMessageContaining("picture's city cannot be one of the choices");
    }

  }

  private Question validQuestion() {
    Picture picture = new Picture();
    picture.setUrl("picture url");
    picture.setCityId(4L);
    Set<Long> threeChoices = Set.of(1L, 2L, 3L);
    return new Question(TEST_ID, picture, threeChoices);
  }

  @Test
  void pictureUrlIsAccessible() {
    Question question = validQuestion();
    assertThat(question.pictureUrl()).isNotNull();
  }

  @Test
  void canAnswerQuestion() {
    Question question = validQuestion();
    long correctAnswer = 4L;
    long wrongAnswer = 3L;

    assertThat(question.isCorrectAnswer(correctAnswer)).isTrue();
    assertThat(question.isCorrectAnswer(wrongAnswer)).isFalse();
  }

  @Test
  void has4Choices() {
    Question question = validQuestion();

    assertThat(question.choices()).isNotEmpty();
    assertThat(question.choices()).hasSize(4);
  }

  @Test
  void questionIsCreatedActiveByDefault() {
    Question question = validQuestion();

    assertThat(question.isActive()).isTrue();
  }
}
