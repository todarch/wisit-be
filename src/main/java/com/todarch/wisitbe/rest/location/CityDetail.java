package com.todarch.wisitbe.rest.location;

import lombok.Data;

@Data
public class CityDetail {
  private long id;
  private String cityName;
  private String countryName;
  private long numberOfPictures;
}
