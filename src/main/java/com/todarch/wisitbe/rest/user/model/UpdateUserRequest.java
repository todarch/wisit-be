package com.todarch.wisitbe.rest.user.model;

import com.todarch.wisitbe.domain.user.User;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateUserRequest {
  @Pattern(regexp = User.USERNAME_PATTERN)
  private String username;
}
