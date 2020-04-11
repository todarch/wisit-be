package com.todarch.wisitbe.explorer.flickr.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FlickrErrorResponse extends FlickrResponse {
  private long code;
  private String message;
}
