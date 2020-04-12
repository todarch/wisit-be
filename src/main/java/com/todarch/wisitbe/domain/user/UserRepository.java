package com.todarch.wisitbe.domain.user;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByIp(String ip);

  Optional<User> findByUsername(String username);
}
