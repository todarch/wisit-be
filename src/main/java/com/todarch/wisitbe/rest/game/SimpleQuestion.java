package com.todarch.wisitbe.rest.game;

import java.util.List;
import lombok.Data;

@Data
public class SimpleQuestion {
  private String picUrl;
  private String answer;
  private List<String> choices;
  private String info;
}
