package com.todarch.wisitbe.data;

import com.todarch.wisitbe.domain.picture.Picture;
import java.util.concurrent.ThreadLocalRandom;

class PictureData {

  static Picture newPicture() {
    Picture picture = new Picture();
    picture.setId(ThreadLocalRandom.current().nextLong());
    picture.setCityId(63L);
    picture.setUrl("TEST_PIC_URL");
    picture.setActive(true);
    return picture;
  }
}
