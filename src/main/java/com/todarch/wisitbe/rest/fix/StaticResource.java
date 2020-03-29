package com.todarch.wisitbe.rest.fix;

import com.todarch.wisitbe.domain.fix.City;
import com.todarch.wisitbe.domain.fix.CityRepository;
import com.todarch.wisitbe.domain.fix.Continent;
import com.todarch.wisitbe.domain.fix.ContinentRepository;
import com.todarch.wisitbe.domain.fix.Country;
import com.todarch.wisitbe.domain.fix.CountryRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/static-data")
@AllArgsConstructor
public class StaticResource {

  private ContinentRepository continentRepository;
  private CountryRepository countryRepository;
  private CityRepository cityRepository;

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
    List<City> all = cityRepository.findAll();
    return ResponseEntity.ok(all);
  }

}
