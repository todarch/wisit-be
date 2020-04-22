package com.todarch.wisitbe.data;

import com.todarch.wisitbe.domain.question.Question;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionData {

  /**
   * New question constructor.
   */
  public static Question newQuestion() {
    Question question =
        new Question(UUID.randomUUID(), PictureData.newPicture(), Set.of(1L, 2L, 3L));

    return question;
  }

  /**
   * Generates a list of questions.
   */
  public static List<Question> questions(int size) {
    return IntStream.range(0, size)
        .mapToObj(x -> newQuestion())
        .collect(Collectors.toList());
  }
}
