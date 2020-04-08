package com.todarch.wisitbe.application.location;

import com.todarch.wisitbe.domain.location.City;
import com.todarch.wisitbe.domain.location.CityRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LocationManager {

  private final CityRepository cityRepository;

  private List<City> cities;

  private Map<Long, String> citiesById;

  private List<Long> cityIds;

  @PostConstruct
  private void loadData() {
    cities = cityRepository.findAllByOrderByName();
    citiesById = cities.stream()
        .collect(Collectors.toUnmodifiableMap(City::getId, City::getName));

    cityIds = new ArrayList<>(citiesById.keySet());
  }

  /**
   * Prepares other choices to be used with correct answer.
   */
  public Set<Long> prepareChoices(long answerCityId) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Set<Long> choices = new HashSet<>(3);

    while (choices.size() != 3) {
      int randomIndex = random.nextInt(cityIds.size());
      Long randomId = cityIds.get(randomIndex);
      if (randomId != answerCityId) {
        choices.add(randomId);
      }
    }

    return choices;
  }

  /**
   * Maps city ids to city names for ui.
   */
  public Set<String> toCityNames(Set<Long> cityIds) {
    return cityIds.stream()
        .map(cityId -> citiesById.get(cityId))
        .collect(Collectors.toSet());
  }

  public City getCityById(long answerCityId) {
    return cityRepository.findById(answerCityId).orElse(null);
  }

  public List<City> getCities() {
    return Collections.unmodifiableList(cities);
  }
}
