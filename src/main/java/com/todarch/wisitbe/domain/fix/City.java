package com.todarch.wisitbe.domain.fix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Table(name = "cities")
@Entity
@Getter
public class City {

  @Id
  private Long id;

  @Column
  private String name;

  @Column
  private String countryId;
}
