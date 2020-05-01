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
    questionReactionRepository.findLikeByUserIdAndQuestionId(userId, questionId)
        .ifPresent(questionReactionRepository::delete);
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
    questionReactionRepository.findDislikeByUserIdAndQuestionId(userId, questionId)
        .ifPresent(questionReactionRepository::delete);
  }

  /**
   * Collects statistics on a question together with user's reaction.
   */
  public QuestionReactionStats stats(String userId, String questionId) {
    QuestionReactionStats stats = questionStats(questionId);

    questionReactionRepository.findByUserIdAndQuestionId(userId, questionId)
        .ifPresent(questionReaction -> {
          stats.setLiked(questionReaction.liked());
          stats.setDisliked(!questionReaction.liked());
        });

    return stats;
  }

  /**
   * Gathers stats without taking into account current user.
   */
  public QuestionReactionStats stats(String questionId) {
    QuestionReactionStats stats = questionStats(questionId);
    stats.setLiked(false);
    stats.setDisliked(false);
    return stats;
  }

  private QuestionReactionStats questionStats(String questionId) {
    QuestionReactionStats stats = new QuestionReactionStats();
    stats.setLikes(questionReactionRepository.countLikesByQuestionId(questionId));
    stats.setDislikes(questionReactionRepository.countDislikesByQuestionId(questionId));
    return stats;
  }
}
