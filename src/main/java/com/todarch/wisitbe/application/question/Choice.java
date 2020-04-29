package com.todarch.wisitbe.application.question;

import lombok.Data;

@Data
public class Choice {
  private final long id;
  private final String cityName;
  private final String countryName;
}
