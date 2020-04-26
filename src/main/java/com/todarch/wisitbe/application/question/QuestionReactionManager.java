package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.domain.question.QuestionReaction;
import com.todarch.wisitbe.domain.question.QuestionReactionRepository;
import com.todarch.wisitbe.rest.question.QuestionReactionStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class QuestionReactionManager {

  private final QuestionReactionRepository questionReactionRepository;

  /**
   * A user likes a question.
   * Turns existing dislike into like.
   */
  public void like(String userId, String questionId) {
    QuestionReaction questionReaction =
        questionReactionRepository.findByUserIdAndQuestionId(userId, questionId)
            .orElseGet(() -> QuestionReaction.likeWith(userId, questionId));

    questionReaction.like();

    questionReactionRepository.save(questionReaction);
  }

  public void unlike(String userId, String questionId) {
    //TODO
  }

  /**
   * A user dislikes a question.
   * Turns existing like into dislike.
   */
  public void dislike(String userId, String questionId) {
    QuestionReaction questionReaction =
        questionReactionRepository.findByUserIdAndQuestionId(userId, questionId)
            .orElseGet(() -> QuestionReaction.dislikeWith(userId, questionId));

    questionReaction.dislike();

    questionReactionRepository.save(questionReaction);
  }

  public void undislike(String userId, String questionId) {
    //TODO
  }

  /**
   * Collects statistics on a question together with user's reaction.
   */
  public QuestionReactionStats stats(String userId, String questionId) {
    QuestionReactionStats stats = new QuestionReactionStats();
    stats.setLikes(questionReactionRepository.countLikesByQuestionId(questionId));
    stats.setDislikes(questionReactionRepository.countDislikesByQuestionId(questionId));

    questionReactionRepository.findByUserIdAndQuestionId(userId, questionId)
        .ifPresent(questionReaction -> {
          stats.setLiked(questionReaction.liked());
          stats.setDisliked(!questionReaction.liked());
        });

    return stats;
  }
}
