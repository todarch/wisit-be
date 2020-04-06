package com.todarch.wisitbe.application.staticdata;

import com.todarch.wisitbe.domain.fix.City;
import com.todarch.wisitbe.domain.fix.CityRepository;
import java.util.ArrayList;
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
public class StaticDataManager {

  private final CityRepository cityRepository;

  private List<City> cities;

  private Map<Long, String> citiesById;

  private List<Long> cityIds;

  @PostConstruct
  private void loadData() {
    cities = cityRepository.findAll();
    citiesById = cities.stream()
        .collect(Collectors.toUnmodifiableMap(City::getId, City::getName));

    cityIds = new ArrayList<>(citiesById.keySet());
  }

  public Long tryToFindCityId(String givenCity) {
    return cities.stream()
        .filter(city -> city.getName().equalsIgnoreCase(givenCity))
        .findFirst()
        .orElseThrow(() -> new RuntimeException("Not-existing city in system: " + givenCity))
        .getId();
  }

  public Set<Long> prepareChoices(long answerCityId) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Set<Long> choices = new HashSet<>(3);

    while (choices.size() != 3) {
      int randomIndex = random.nextInt(cityIds.size());
      Long randomId = cityIds.get(randomIndex);
      choices.add(randomId);
    }

    return choices;
  }

  public Set<String> toCityNames(Set<Long> choices) {
    return choices.stream()
        .map(cityId -> citiesById.get(cityId))
        .collect(Collectors.toSet());
  }

  public City getCityById(long answerCityId) {
    return cityRepository.findById(answerCityId).get();
  }
}
