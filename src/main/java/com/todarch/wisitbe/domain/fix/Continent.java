package com.todarch.wisitbe.domain.fix;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Table(name = "continents")
@Entity
@Getter
public class Continent {

  @Id
  private Long id;

  @Column
  private String name;
}
