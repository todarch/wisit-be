package com.todarch.wisitbe.application.location;

import com.todarch.wisitbe.domain.location.City;
import com.todarch.wisitbe.domain.location.CityRepository;
import com.todarch.wisitbe.domain.location.Country;
import com.todarch.wisitbe.domain.location.CountryRepository;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.DuplicateResourceException;
import com.todarch.wisitbe.rest.location.AddCityCmd;
import com.todarch.wisitbe.rest.location.CityDetail;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
public class LocationManager {

  private final CityRepository cityRepository;

  private final CountryRepository countryRepository;

  private final PictureRepository pictureRepository;

  /**
   * Prepares other choices to be used with correct answer.
   */
  public Set<Long> prepareChoices(long answerCityId) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Set<Long> choices = new HashSet<>(3);

    var cities = cityRepository.findAllByOrderByName();

    while (choices.size() != 3) {
      int randomIndex = random.nextInt(cities.size());
      Long randomId = cities.get(randomIndex).getId();
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
        .map(cityId -> cityRepository.getById(cityId).getName())
        .collect(Collectors.toSet());
  }

  public City getCityById(long answerCityId) {
    return cityRepository.findById(answerCityId).orElse(null);
  }

  public List<City> cities() {
    return cityRepository.findAllByOrderByName();
  }

  /**
   * Converts city ids to city objects.
   */
  public Set<City> toCities(Set<Long> cityIds) {
    return cityIds.stream()
        .map(this::getCityById)
        .collect(Collectors.toSet());
  }

  public List<Country> countries() {
    return countryRepository.findAllByOrderByName();
  }

  /**
   * Returns the list of cities with their details.
   */
  public List<CityDetail> citiesWithDetail() {
    return cityRepository.findAllByOrderByName().stream()
        .map(this::toCityDetail)
        .sorted(Comparator.comparingLong(CityDetail::getNumberOfPictures))
        .collect(Collectors.toList());
  }

  private CityDetail toCityDetail(City city) {
    CityDetail cityDetail = new CityDetail();
    cityDetail.setId(city.getId());
    cityDetail.setCityName(city.getName());
    cityDetail.setCountryName(countryRepository.getById(city.getCountryId()).getName());
    cityDetail.setNumberOfPictures(pictureRepository.countAllActiveByCityId(city.getId()));
    return cityDetail;
  }

  /**
   * Adds new city.
   */
  public void addCity(@NonNull AddCityCmd addCityCmd) {
    Country country = countryRepository.getById(addCityCmd.getCountryId());

    String formattedCityName = StringUtils.capitalize(addCityCmd.getCityName().toLowerCase());

    if (cityRepository.findByName(formattedCityName).isPresent()) {
      throw new DuplicateResourceException(formattedCityName + " already exist.");
    }

    City newCity = country.addCity(formattedCityName);

    cityRepository.save(newCity);
  }
}
