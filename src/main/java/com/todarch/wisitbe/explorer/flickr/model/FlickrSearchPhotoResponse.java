package com.todarch.wisitbe.explorer.flickr.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FlickrSearchPhotoResponse extends FlickrResponse {
  private FlickrPhotos photos;
}
