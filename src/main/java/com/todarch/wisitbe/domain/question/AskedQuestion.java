package com.todarch.wisitbe.domain.question;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "asked_questions")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED) // for hibernate
@Setter(AccessLevel.PROTECTED)
@Getter
public class AskedQuestion {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String userId;

  @Column
  private String questionId;

  @Column
  private boolean knew;

  @Column
  private long answeredInSeconds = 0;

  @Column
  private int scoreDelta;

  @Column
  private LocalDateTime createdAt;
}
