package com.todarch.wisitbe.rest.location;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.location.City;
import com.todarch.wisitbe.domain.location.Continent;
import com.todarch.wisitbe.domain.location.ContinentRepository;
import com.todarch.wisitbe.domain.location.Country;
import com.todarch.wisitbe.domain.location.CountryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/static-data")
@AllArgsConstructor
public class StaticDataResource {

  private ContinentRepository continentRepository;
  private CountryRepository countryRepository;

  private final LocationManager locationManager;

  @GetMapping("/continents")
  public ResponseEntity<List<Continent>> continents() {
    List<Continent> all = continentRepository.findAll();
    return ResponseEntity.ok(all);
  }

  @GetMapping("/countries")
  public ResponseEntity<List<Country>> countries() {
    List<Country> all = countryRepository.findAll();
    return ResponseEntity.ok(all);
  }

  @GetMapping("/cities")
  public ResponseEntity<List<City>> cities() {
    return ResponseEntity.ok(locationManager.getCities());
  }

}
