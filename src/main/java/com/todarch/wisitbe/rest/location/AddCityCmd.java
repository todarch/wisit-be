package com.todarch.wisitbe.rest.location;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AddCityCmd {
  @Min(1)
  private long countryId;
  @Size(min = 3, max = 100)
  private String cityName;
}
