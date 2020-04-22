package com.todarch.wisitbe.domain.user;

import com.todarch.wisitbe.infrastructure.rest.errorhandling.ResourceNotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByIp(String ip);

  default User getById(String userId) {
    return findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  Optional<User> findByUsername(String username);
}
