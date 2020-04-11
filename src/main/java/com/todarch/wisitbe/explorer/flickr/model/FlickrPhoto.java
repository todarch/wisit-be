package com.todarch.wisitbe.explorer.flickr.model;

import lombok.Data;

@Data
public class FlickrPhoto {
  // "https://farm{farm}.staticflickr.com/{server}/{id}_{secret}_{size:b|s}.jpg
  private static final String PICTURE_URL_FORMAT = "https://farm%d.staticflickr.com/%s/%s_%s_b.jpg";
  private String id;
  private String owner;
  private String secret;
  private String server;
  private long farm;

  /**
   * Constructs the url from photo data.
   */
  public String toUrl() {
    return String.format(PICTURE_URL_FORMAT,
        farm,
        server,
        id,
        secret
        );
  }
}
