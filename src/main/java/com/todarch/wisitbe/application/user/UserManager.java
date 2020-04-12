package com.todarch.wisitbe.application.user;

import com.todarch.wisitbe.domain.user.User;
import com.todarch.wisitbe.domain.user.UserRepository;
import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import com.todarch.wisitbe.rest.user.model.UpdateUserRequest;
import com.todarch.wisitbe.rest.user.model.UserProfile;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserManager {

  private final UserRepository userRepository;

  /**
   * Updates username information.
   * @param userId whose information is being updated
   * @param updateUserRequest values to be updated
   */
  public void updateUser(@NonNull String userId,
                         @NonNull UpdateUserRequest updateUserRequest) {

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User does not exist: " + userId));

    user.setUsername(updateUserRequest.getUsername());

    userRepository.save(user);
  }

  public boolean isUsernameUnique(String username) {
    return userRepository.findByUsername(username).isEmpty();
  }

  /**
   * Returns a user profile information.
   */
  public UserProfile profile(String userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User does not exist: " + userId));

    UserProfile userProfile = new UserProfile();
    userProfile.setUsername(user.getUsername());

    return userProfile;
  }
}
