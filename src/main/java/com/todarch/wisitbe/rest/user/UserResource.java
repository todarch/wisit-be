package com.todarch.wisitbe.rest.user;

import com.todarch.wisitbe.application.user.UserManager;
import com.todarch.wisitbe.infrastructure.security.CurrentUser;
import com.todarch.wisitbe.infrastructure.security.CurrentUserProvider;
import com.todarch.wisitbe.rest.user.model.UpdateUserRequest;
import com.todarch.wisitbe.rest.user.model.UserProfile;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserResource {

  private final UserManager userManager;
  private final CurrentUserProvider currentUserProvider;

  /**
   * Returns current user profile details.
   */
  @GetMapping("/profile")
  public ResponseEntity<UserProfile> currentUserProfile() {
    CurrentUser currentUser = currentUserProvider.currentUser();
    UserProfile profile = userManager.profile(currentUser.id());
    return ResponseEntity.ok(profile);
  }

  /**
   * Utility endpoint for clients to check uniqueness of entered username.
   */
  @GetMapping("/{username}/unique")
  public ResponseEntity<Void> isUsernameUnique(@PathVariable String username) {
    if (userManager.isUsernameUnique(username)) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok().build();
    }
  }

  @PutMapping
  public void selectUsername(@Valid @RequestBody UpdateUserRequest userRequest) {
    CurrentUser currentUser = currentUserProvider.currentUser();
    userManager.updateUser(currentUser.id(), userRequest);
  }
}
