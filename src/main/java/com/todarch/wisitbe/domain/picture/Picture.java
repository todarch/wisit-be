package com.todarch.wisitbe.domain.picture;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "pictures")
@Entity
@Data
public class Picture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String url;

  private long cityId;

  public boolean isFrom(long answerCityId) {
    return cityId == answerCityId;
  }
}
