package com.todarch.wisitbe.application.picture;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.domain.location.City;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.PictureCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.InvalidInputException;
import com.todarch.wisitbe.rest.picture.NewPictureReq;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PictureManager {

  private final PictureRepository pictureRepository;

  private final LocationManager locationManager;

  private final WisitEventPublisher wisitEventPublisher;


  /**
   * Adds new picture.
   * Creates an event for the operation so it can be reacted to.
   * @throws InvalidInputException a picture url can not be added again
   * @throws InvalidInputException a picture url must be added with a known city
   */
  public Picture addNewPicture(NewPictureReq pictureReq) {
    if (pictureRepository.findByUrl(pictureReq.getPicUrl()).isPresent()) {
      throw new InvalidInputException("Picture url is already added.");
    }

    City city = locationManager.getCityById(pictureReq.getCityId());
    if (city == null) {
      throw new InvalidInputException("City does not exist in the system.");
    }

    Picture newPic = new Picture();
    newPic.setUrl(pictureReq.getPicUrl());
    newPic.setCityId(pictureReq.getCityId());
    Picture savedPic = pictureRepository.save(newPic);

    PictureCreatedEvent pictureCreatedEvent = new PictureCreatedEvent();
    pictureCreatedEvent.setCreatedPictureId(savedPic.getId());
    wisitEventPublisher.publishEvent(pictureCreatedEvent);

    return savedPic;
  }

  public Optional<Picture> getById(long id) {
    return pictureRepository.findById(id);
  }
}
