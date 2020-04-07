package com.todarch.wisitbe.application.picture;

import com.todarch.wisitbe.application.staticdata.StaticDataManager;
import com.todarch.wisitbe.domain.fix.City;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.infrastructure.messaging.event.PictureCreatedEvent;
import com.todarch.wisitbe.infrastructure.messaging.publisher.WisitEventPublisher;
import com.todarch.wisitbe.rest.picture.NewPictureReq;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PictureManager {

  private final PictureRepository pictureRepository;

  private final StaticDataManager staticDataManager;

  private final WisitEventPublisher wisitEventPublisher;

  public Picture next() {
    List<Picture> all = pictureRepository.findAll();
    return all.get(ThreadLocalRandom.current().nextInt(all.size()));
  }

  public Picture newPicture(NewPictureReq pictureReq) {
    if (pictureRepository.findByUrl(pictureReq.getPicUrl()).isPresent()) {
      throw new RuntimeException("Picture url is already added.");
    }
    City city = staticDataManager.getCityById(pictureReq.getCityId());
    if (city == null) {
      throw new RuntimeException("City does not exist in the system.");
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
