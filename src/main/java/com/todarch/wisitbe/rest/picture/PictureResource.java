package com.todarch.wisitbe.rest.picture;

import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.domain.picture.Picture;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pictures")
@AllArgsConstructor
public class PictureResource {

  private PictureManager pictureManager;

  @GetMapping("/next")
  public ResponseEntity<Picture> next() {
    Picture next = pictureManager.next();
    return ResponseEntity.ok(next);
  }
}
