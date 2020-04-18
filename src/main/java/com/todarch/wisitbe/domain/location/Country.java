package com.todarch.wisitbe.domain.location;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Table(name = "countries")
@Entity
@Getter
public class Country {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column
  private String continentId;

  /**
   * City constructor method.
   */
  public City addCity(String cityName) {
    if (id == null) {
      throw new RuntimeException("Cannot add city to unknown country");
    }

    City city = new City();
    city.setCountryId(id);
    city.setName(cityName);
    return city;
  }
}
