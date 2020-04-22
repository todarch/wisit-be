package com.todarch.wisitbe.domain.question;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "user_questions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for hibernate
@Setter(AccessLevel.PROTECTED)
@Getter
public class UserQuestion {
  protected static final int DEFAULT_WEIGHT = 11;

  protected static final int ANSWERED_CORRECT_COUNT_LIMIT = 5;

  @Id
  private String id;

  @Column
  private String userId;

  @OneToOne(cascade = CascadeType.PERSIST)
  @JoinColumn(name = "question_id", referencedColumnName = "id")
  private Question question;

  @Column
  private int weight = DEFAULT_WEIGHT;

  @Column
  private LocalDateTime lastAskedAt;

  public boolean isNew() {
    return weight == DEFAULT_WEIGHT;
  }

  /**
   * Answers this question.
   */
  public boolean answer(long givenAnswer) {
    lastAskedAt = LocalDateTime.now();
    if (question.isCorrectAnswer(givenAnswer)) {
      weight = weight - 2;
      return true;
    } else {
      weight = weight == DEFAULT_WEIGHT ? DEFAULT_WEIGHT - 1 : weight;
      return false;
    }
  }

  public boolean canBeAskedAgain() {
    return weight > 1;
  }

  public long correctAnswer() {
    return question.answerCityId();
  }

  protected Optional<LocalDateTime> lastAskedAt() {
    return Optional.ofNullable(lastAskedAt);
  }
}
