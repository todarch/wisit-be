package com.todarch.wisitbe.rest.picture;

import lombok.Data;

@Data
public class NewPictureReq {
  private String picUrl;
  private long cityId;
}
