package com.todarch.wisitbe.explorer.resource;

import com.todarch.wisitbe.explorer.application.ExplorerManager;
import com.todarch.wisitbe.infrastructure.aspect.InternalOnly;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/explorer")
@AllArgsConstructor
public class ExplorerResource {

  private final ExplorerManager explorerManager;

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
}
