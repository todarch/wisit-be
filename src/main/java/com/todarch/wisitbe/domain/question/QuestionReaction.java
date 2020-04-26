package com.todarch.wisitbe.domain.question;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Table(name = "question_reactions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for hibernate
public class QuestionReaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String userId;

  @Column
  private String questionId;

  @Column
  private boolean liked;

  @Column
  private LocalDateTime createdAt;

  private QuestionReaction(String userId,
                           String questionId,
                           boolean liked) {
    this.userId = userId;
    this.questionId = questionId;
    this.liked = liked;
    createdAt = LocalDateTime.now();
  }

  public static QuestionReaction likeWith(@NonNull String userId, @NonNull String questionId) {
    return new QuestionReaction(userId, questionId, true);
  }

  public static QuestionReaction dislikeWith(@NonNull String userId, @NonNull String questionId) {
    return new QuestionReaction(userId, questionId, false);
  }

  public void like() {
    this.liked = true;
  }

  public void dislike() {
    this.liked = false;
  }

  public boolean liked() {
    return this.liked;
  }
}
