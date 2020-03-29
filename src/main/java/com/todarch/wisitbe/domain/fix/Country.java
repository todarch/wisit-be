package com.todarch.wisitbe.domain.fix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Table(name = "countries")
@Entity
@Getter
public class Country {

  @Id
  private Long id;

  @Column
  private String name;

  @Column
  private String continentId;
}
