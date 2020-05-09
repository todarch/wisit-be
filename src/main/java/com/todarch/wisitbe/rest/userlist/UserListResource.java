package com.todarch.wisitbe.rest.userlist;

import com.todarch.wisitbe.application.userlist.SavedListItem;
import com.todarch.wisitbe.application.userlist.UserListManager;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected/user-lists")
@AllArgsConstructor
public class UserListResource {
  private final CurrentUserProvider currentUserProvider;
  private final UserListManager userListManager;

  @Data
  private static class SaveItemCmd {
    private long pictureId;
  }

  /**
   * Saves a new picture to users saved list.
   */
  @PostMapping("/saved")
  public ResponseEntity<Void> save(@RequestBody SaveItemCmd saveItemCmd) {
    String userId = currentUserProvider.currentUser().id();
    userListManager.save(userId, saveItemCmd.pictureId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Gets the list of saved pictures.
   */
  @GetMapping("/saved")
  public ResponseEntity<List<SavedListItem>> savedList() {
    String userId = currentUserProvider.currentUser().id();
    List<SavedListItem> savedListItems = userListManager.savedList(userId);
    return ResponseEntity.ok(savedListItems);
  }

  /**
   * Removes picture from user's saved list.
   */
  @DeleteMapping("/saved/{pictureId}")
  public ResponseEntity<Void> removeFromSaved(@PathVariable long pictureId) {
    String userId = currentUserProvider.currentUser().id();
    userListManager.removeFromSaved(userId, pictureId);
    return ResponseEntity.ok().build();
  }

  /**
   * Checks if the picture is in user's saved list.
   */
  @GetMapping("/saved/{pictureId}")
  public ResponseEntity<Void> isSaved(@PathVariable long pictureId) {
    String userId = currentUserProvider.currentUser().id();
    if (userListManager.isSaved(userId, pictureId)) {
      return ResponseEntity.ok().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

}
