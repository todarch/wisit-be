package com.todarch.wisitbe.application.userlist;

import com.todarch.wisitbe.application.picture.DetailedPicture;
import com.todarch.wisitbe.application.picture.PictureManager;
import com.todarch.wisitbe.domain.picture.Picture;
import com.todarch.wisitbe.domain.picture.PictureRepository;
import com.todarch.wisitbe.domain.user.list.ListedPicture;
import com.todarch.wisitbe.domain.user.list.ListedPictureRepository;
import com.todarch.wisitbe.domain.user.list.UserList;
import com.todarch.wisitbe.domain.user.list.UserListRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserListManager {
  private final PictureRepository pictureRepository;
  private final ListedPictureRepository listedPictureRepository;
  private final UserListRepository userListRepository;
  private final PictureManager pictureManager;

  /**
   * Saves picture to user's saved list.
   */
  public void save(String userId, long pictureId) {
    UserList userList = userListRepository.getByIdAndUserId("ignored", userId);
    Picture picture = pictureRepository.getById(pictureId);

    ListedPicture listedPicture = userList.list(picture);
    listedPictureRepository.save(listedPicture);
  }

  /**
   * Prepares list of pictures in users saved list.
   */
  public List<SavedListItem> savedList(String userId) {
    List<Long> pictureIds = listedPictureRepository.findAllByUserId(userId)
        .stream()
        .map(ListedPicture::pictureId)
        .collect(Collectors.toList());

    return pictureManager.pictures(pictureIds)
        .stream()
        .map(this::toSavedListItem)
        .collect(Collectors.toList());
  }

  private SavedListItem toSavedListItem(DetailedPicture detailedPicture) {
    SavedListItem savedListItem = new SavedListItem();
    savedListItem.setPicId(detailedPicture.getPicId());
    savedListItem.setPicUrl(detailedPicture.getPicUrl());
    savedListItem.setCityName(detailedPicture.getCityName());
    return savedListItem;
  }

  public void removeFromSaved(String userId, long pictureId) {
    listedPictureRepository.deleteByUserIdAndPictureId(userId, pictureId);
  }

  public boolean isSaved(String userId, long pictureId) {
    return listedPictureRepository.findByUserIdAndPictureId(userId, pictureId).isPresent();
  }
}
