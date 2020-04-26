package com.todarch.wisitbe.domain.question;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class QuestionReactionTest {

  private static final String QUESTION_ID = "questionId";
  private static final String USER_ID = "userId";

  @Test
  void createsLikeReaction() {
    QuestionReaction reaction = QuestionReaction.likeWith(USER_ID, QUESTION_ID);
    assertThat(reaction.liked()).isTrue();
  }

  @Test
  void createsDislikeReaction() {
    QuestionReaction reaction = QuestionReaction.dislikeWith(USER_ID, QUESTION_ID);
    assertThat(reaction.liked()).isFalse();
  }

  @Test
  void canLike() {
    QuestionReaction reaction = QuestionReaction.dislikeWith(USER_ID, QUESTION_ID);
    reaction.like();
    assertThat(reaction.liked()).isTrue();
  }
}
