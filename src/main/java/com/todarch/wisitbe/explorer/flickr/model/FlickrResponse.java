package com.todarch.wisitbe.explorer.flickr.model;

import lombok.Data;

@Data
public abstract class FlickrResponse {
  private String stat;

  public boolean isOk() {
    return "ok".equals(stat);
  }
}
