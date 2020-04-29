package com.todarch.wisitbe.rest.question;

import com.todarch.wisitbe.application.question.Choice;
import lombok.Data;

@Data
public class QuestionAnswer {
  private Choice correctChoice;
  private Choice givenChoice;
  private boolean knew;
  private int scoreDelta;
}
