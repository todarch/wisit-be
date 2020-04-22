package com.todarch.wisitbe.domain.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.todarch.wisitbe.data.QuestionData;
import com.todarch.wisitbe.data.UserData;
import com.todarch.wisitbe.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class UserQuestionRepositoryTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private UserQuestionRepository userQuestionRepository;

  @Test
  void deletesAllUserQuestionsByQuestionId() {
    User testUser = UserData.newUser();
    Question testQuestion = QuestionData.newQuestion();
    UserQuestion userQuestion =
        UserQuestionFactory.createQuestionForUser(testUser.id(), testQuestion);

    testEntityManager.persistAndFlush(userQuestion);

    assertThat(userQuestionRepository.findById(userQuestion.getId())).isPresent();

    userQuestionRepository.deleteAllByQuestionId(testQuestion.getId());

    assertThat(userQuestionRepository.findById(userQuestion.getId())).isEmpty();
  }

}
