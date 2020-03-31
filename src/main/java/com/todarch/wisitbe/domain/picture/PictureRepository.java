package com.todarch.wisitbe.domain.picture;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class PictureRepository {

  private static List<Picture> pictures;


  static {
    pictures = new ArrayList<>();

    Picture stockholm = new Picture();
    stockholm.setUrl("https://farm8.staticflickr.com/7385/8731881776_92d7bf6603_b.jpg");
    stockholm.setCityId(37L);
    pictures.add(stockholm);

    Picture istanbul = new Picture();
    istanbul.setUrl("https://farm9.staticflickr.com/8667/15491990730_83ce51863a_b.jpg");
    istanbul.setCityId(2L);
    pictures.add(istanbul);

    Picture brussels = new Picture();
    brussels.setUrl("https://live.staticflickr.com/2434/3800800292_867dcce9e0_c.jpg");
    brussels.setCityId(6L);
    pictures.add(brussels);
  }

  public List<Picture> findAll() {
    return pictures;
  }

}
