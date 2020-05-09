package com.todarch.wisitbe.domain.user.list;

import com.todarch.wisitbe.domain.picture.Picture;

public class UserList {
  private long id;

  private String userId;

  /**
   * Static factory method.
   */
  public static UserList of(String userId) {
    UserList userList = new UserList();
    userList.userId = userId;
    return userList;
  }

  /**
   * Lists picture under default saved list.
   */
  public ListedPicture list(Picture picture) {
    ListedPicture listedPicture = new ListedPicture();
    listedPicture.setUserId(userId);
    listedPicture.setPictureId(picture.getId());
    // listedPicture.setListId(id);
    return listedPicture;
  }
}
