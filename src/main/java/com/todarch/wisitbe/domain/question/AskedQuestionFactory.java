package com.todarch.wisitbe.domain.question;

public final class AskedQuestionFactory {

  private AskedQuestionFactory() {
    throw new AssertionError("No instance of utility class");
  }

  /**
   * Creates a question that is already asked and answered question.
   */
  public static AskedQuestion create(UserQuestion userQuestion, boolean knew) {
    AskedQuestion askedQuestion = new AskedQuestion();
    askedQuestion.setUserId(userQuestion.getUserId());
    askedQuestion.setQuestionId(userQuestion.getQuestion().getId());
    askedQuestion.setKnew(knew);
    return askedQuestion;
  }
}
