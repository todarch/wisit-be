package com.todarch.wisitbe.explorer.application;

import com.todarch.wisitbe.application.location.LocationManager;
import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.domain.location.City;
import com.todarch.wisitbe.explorer.flickr.FlickrClient;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.InvalidInputException;
import com.todarch.wisitbe.rest.picture.NewPictureReq;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExplorerManager {

  private final LocationManager locationManager;

  private final FlickrClient flickrClient;

  private final PictureManager pictureManager;

  /**
   * Explores pictures from providers with a limited set of input.
   */
  public void explorePicturesLimited() {
    int limit = 5;
    List<City> cities = locationManager.cities();
    ThreadLocalRandom current = ThreadLocalRandom.current();
    int fromIndex = current.nextInt(cities.size() - limit);
    retrievePicturesOf(cities.subList(fromIndex, fromIndex + 5));
  }

  public void explorePictures() {
    retrievePicturesOf(locationManager.cities());
  }

  private void retrievePicturesOf(List<City> cities) {
    cities.forEach(city -> {
      List<String> searchPhotos = flickrClient.searchPhotos(city.getName());
      searchPhotos.forEach(picUrl -> {
        try {
          NewPictureReq newPictureReq = new NewPictureReq();
          newPictureReq.setPicUrl(picUrl);
          newPictureReq.setCityId(city.getId());
          pictureManager.addNewPicture(newPictureReq);
        } catch (InvalidInputException ex) {
          // ignore if picture url already exists
        }
      });
    });
  }
}
