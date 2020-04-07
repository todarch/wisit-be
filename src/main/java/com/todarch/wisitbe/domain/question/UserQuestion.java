package com.todarch.wisitbe.domain.question;

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

  @Id
  private String id;

  @Column
  private String userId;

  @OneToOne
  @JoinColumn(name = "question_id", referencedColumnName = "id")
  private Question question;
}
