package com.todarch.wisitbe.domain.picture;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Table(name = "pictures")
@Entity
@Getter
public class Picture {

  @Id
  private long id;

  private String url;

  private long cityId;

}
