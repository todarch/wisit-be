package com.todarch.wisitbe.domain.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Table(name = "users")
@Entity
@Data
public class User {

  @Id
  private String id;

  @Column
  private String ip;

  @Column
  private String userAgent;


}
