package com.todarch.wisitbe.explorer.resource;

import com.todarch.wisitbe.explorer.application.ExplorerManager;
import com.todarch.wisitbe.explorer.flickr.FlickrClient;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/explorer")
@AllArgsConstructor
public class ExplorerResource {

  private final ExplorerManager explorerManager;

  private final FlickrClient flickrClient;

  @InternalOnly
  @PostMapping("/explore/limited")
  public void retrievePictureForOneCity() {
    explorerManager.explorePicturesLimited();
  }

  @InternalOnly
  @PostMapping("/explore/all")
  public void retrievePictureForAllCities() {
    explorerManager.explorePictures();
  }

  @InternalOnly
  @GetMapping("/explore/{cityName}")
  public ResponseEntity<List<String>> searchPhotosFor(@PathVariable String cityName) {
    return ResponseEntity.ok(flickrClient.searchPhotos(cityName));
  }
}
