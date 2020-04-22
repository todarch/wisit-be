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
class AskedQuestionRepositoryTest {

  @Autowired
  private TestEntityManager testEntityManager;

  @Autowired
  private AskedQuestionRepository askedQuestionRepository;

  @Test
  void countsAllByQuestionId() {
    User testUser = UserData.newUser();
    User anotherUser = UserData.newUser();

    Question firstQuestion = QuestionData.newQuestion();

    UserQuestion userQuestion =
        UserQuestionFactory.createQuestionForUser(testUser.id(), firstQuestion);

    UserQuestion anotherUserQuestion =
        UserQuestionFactory.createQuestionForUser(anotherUser.id(), firstQuestion);

    AskedQuestion firstAskedQuestion = AskedQuestionFactory.create(userQuestion, false, 20);
    AskedQuestion secondAskedQuestion = AskedQuestionFactory.create(anotherUserQuestion, false, 20);


    testEntityManager.persistAndFlush(firstAskedQuestion);
    testEntityManager.persistAndFlush(secondAskedQuestion);

    long count = askedQuestionRepository.countByQuestionId(firstQuestion.getId());

    assertThat(count).isEqualTo(2);
  }
}
