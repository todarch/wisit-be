package com.todarch.wisitbe.rest.location;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.location.City;
import com.todarch.wisitbe.domain.location.Country;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/location")
@AllArgsConstructor
public class LocationResource {

  private final LocationManager locationManager;

  @GetMapping("/cities")
  public ResponseEntity<List<City>> cities() {
    return ResponseEntity.ok(locationManager.cities());
  }

  @InternalOnly
  @PostMapping("/cities")
  public ResponseEntity<Void> addCity(@Valid @RequestBody AddCityCmd addCityCmd) {
    locationManager.addCity(addCityCmd);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping("/cities-with-detail")
  public ResponseEntity<List<CityDetail>> citiesWithDetail() {
    return ResponseEntity.ok(locationManager.citiesWithDetail());
  }

  @GetMapping("/countries")
  public ResponseEntity<List<Country>> countries() {
    return ResponseEntity.ok(locationManager.countries());
  }

}
