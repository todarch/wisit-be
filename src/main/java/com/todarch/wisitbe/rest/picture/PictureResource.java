package com.todarch.wisitbe.rest.picture;

import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import java.util.Objects;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pictures")
@AllArgsConstructor
public class PictureResource {

  private PictureManager pictureManager;

  @GetMapping("/next")
  public ResponseEntity<Picture> next() {
    Picture next = pictureManager.next();
    return ResponseEntity.ok(next);
  }

  @GetMapping("/:id")
  public ResponseEntity<Picture> picById(@PathVariable long id) {
    Optional<Picture> optionalPic = pictureManager.getById(id);
    if (optionalPic.isPresent()) {
      return ResponseEntity.ok(optionalPic.get());
    }

    return ResponseEntity.notFound().build();
  }

  @InternalOnly
  @PostMapping
  public ResponseEntity<Picture> newPicture(@RequestBody NewPictureReq picture) {
    Objects.requireNonNull(picture.getUrl(), "pic url cannot be null");
    Picture createdPic = pictureManager.newPicture(picture);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdPic);
  }

  @Data
  public static class NewPictureReq {
    private String url;
    private String city;
  }
}
