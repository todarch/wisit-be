package com.todarch.wisitbe.application.picture;

import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PictureManager {

  private PictureRepository pictureRepository;

  public Picture next() {
    List<Picture> all = pictureRepository.findAll();
    return all.get(ThreadLocalRandom.current().nextInt(all.size()));
  }
}
