package com.todarch.wisitbe.explorer.flickr.model;

import java.util.List;
import lombok.Data;

@Data
public class FlickrPhotos {
  private long page;
  private String pages;
  private long perpage;
  private String total;
  private List<FlickrPhoto> photo;
}
