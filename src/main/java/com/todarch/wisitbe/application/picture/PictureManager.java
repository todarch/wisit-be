package com.todarch.wisitbe.application.picture;

import com.todarch.wisitbe.application.staticdata.StaticDataManager;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
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

  public Picture next() {
    List<Picture> all = pictureRepository.findAll();
    return all.get(ThreadLocalRandom.current().nextInt(all.size()));
  }

  public Picture newPicture(NewPictureReq pictureReq) {
    Picture newPic = new Picture();
    newPic.setUrl(pictureReq.getPicUrl());
    newPic.setCityId(staticDataManager.tryToFindCityId(pictureReq.getCityName()));
    return pictureRepository.save(newPic);
  }

  public Optional<Picture> getById(long id) {
    return pictureRepository.findById(id);
  }
}
