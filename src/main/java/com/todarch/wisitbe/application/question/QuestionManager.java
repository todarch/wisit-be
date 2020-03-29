package com.todarch.wisitbe.application.question;

import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.domain.fix.City;
import com.todarch.wisitbe.domain.fix.CityRepository;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.rest.game.SimpleQuestion;
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
public class QuestionManager {

  private final CityRepository cityRepository;

  private final PictureManager pictureManager;

  private Map<Long, String> citiesById;

  private List<Long> cityIds;

  @PostConstruct
  private void loadData() {
    citiesById = cityRepository.findAll()
        .stream()
        .collect(Collectors.toUnmodifiableMap(City::getId, City::getName));

    cityIds = new ArrayList<>(citiesById.keySet());
  }

  public SimpleQuestion next() {

    Picture next = pictureManager.next();

    SimpleQuestion simpleQuestion = new SimpleQuestion();
    simpleQuestion.setPicUrl(next.getUrl());
    simpleQuestion.setAnswer(citiesById.get(next.getCityId()));
    simpleQuestion.setChoices(prepareChoices(next.getCityId()));

    return simpleQuestion;
  }

  private List<String> prepareChoices(long answerCityId) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    Set<String> choices = new HashSet<>(4);
    choices.add(citiesById.get(answerCityId));

    while (choices.size() != 4) {
      int randomIndex = random.nextInt(cityIds.size());
      Long randomId = cityIds.get(randomIndex);
      choices.add(citiesById.get(randomId));
    }

    return new ArrayList<>(choices);
  }
}
