package com.todarch.wisitbe.domain.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import lombok.NonNull;

@Table(name = "users")
@Entity
@Data //TODO: get rid off setter/getters
public class User {

  public static final String USERNAME_PATTERN = "^[a-z0-9]{6,100}$";

  /**
   * LocalDateTime.MIN does not work when passed as date after.
   * ERROR: timestamp out of range: "169087565-03-15 04:51:43+01".
   * Create a date we know for sure will be oldest in this case.
   */
  public static final LocalDateTime MIN_CREATED_AT_VALUE =
      LocalDateTime.of(LocalDate.ofYearDay(1992, 18), LocalTime.now());

  @Id
  private String id;

  @Column
  private String ip;

  @Column
  private String userAgent;

  @Column(length = 100, nullable = false)
  private String username;

  private LocalDateTime pivotPoint;

  /**
   * we need to do this in order to not loose the constraints on username column for now.
   */
  public void setId(@NonNull String userId) {
    this.id = userId;
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

  public LocalDateTime pivotPoint() {
    return Optional.ofNullable(pivotPoint)
        .orElse(MIN_CREATED_AT_VALUE);
  }

  public String id() {
    return id;
  }

  public boolean isEligibleForMoreQuestions() {
    return LocalDateTime.now().minusHours(1).isAfter(pivotPoint());
  }
}
