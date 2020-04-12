package com.todarch.wisitbe.domain.user;

import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NonNull;

@Table(name = "users")
@Entity
@Data
public class User {

  public static final String USERNAME_PATTERN = "^[a-z0-9]{6,100}$";

  @Id
  private String id;

  @Column
  private String ip;

  @Column
  private String userAgent;

  @Column(length = 100, nullable = false)
  private String username;

  public void setId(@NonNull String userId) {
    this.id = userId;
    // we need to do this in order to not loose the constraints on username column for now
    this.username = userId;
  }

  /**
   * Sets username for this user if already not set it once.
   */
  public void setUsername(@NonNull String username) {
    if (this.username.equals(username)) {
      return;
    }

    if (!didPickedUsername()) {
      this.username = username;
    } else {
      throw new CannotChangeUserNameException("User cannot change username anymore.");
    }
  }

  private boolean didPickedUsername() {
    return !id.equals(username);
  }

  /**
   * Returns username if user has already picked one.
   */
  public Optional<String> pickedUsername() {
    if (didPickedUsername()) {
      return Optional.of(username);
    }
    return Optional.empty();
  }
}
