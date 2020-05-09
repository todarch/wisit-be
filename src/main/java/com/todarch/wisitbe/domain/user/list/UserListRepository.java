package com.todarch.wisitbe.domain.user.list;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class UserListRepository {
  public Optional<UserList> findByIdAndUserId(String id, String userId) {
    return Optional.of(UserList.of(userId));
  }

  public UserList getByIdAndUserId(String id, String userId) {
    return findByIdAndUserId(id, userId)
        .orElseThrow(() -> new ResourceNotFoundException("List not found for user"));
  }
}
