package com.todarch.wisitbe.domain.question;

public class AskedQuestionFactory {

  public static AskedQuestion create(UserQuestion userQuestion, boolean knew) {
    AskedQuestion askedQuestion = new AskedQuestion();
    askedQuestion.setUserId(userQuestion.getUserId());
    askedQuestion.setQuestionId(userQuestion.getQuestion().getId());
    askedQuestion.setKnew(knew);
    return askedQuestion;
  }
}
