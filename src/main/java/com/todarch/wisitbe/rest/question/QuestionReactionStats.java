package com.todarch.wisitbe.rest.question;

import lombok.Data;

@Data
public class QuestionReactionStats {
  private long likes;
  private long dislikes;
  private boolean liked;
  private boolean disliked;
}
