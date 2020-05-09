package com.todarch.wisitbe.domain.user.list;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "listed_pictures")
@Setter(AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ListedPicture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private long pictureId;

  private String userId;

  public long pictureId() {
    return pictureId;
  }
}
