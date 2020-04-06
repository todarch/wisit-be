package com.todarch.wisitbe.domain.question;

import com.todarch.wisitbe.domain.picture.Picture;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NonNull;

@Table(name = "questions")
@Entity
public class Question {

  @Id
  @Getter
  private String id;

  @OneToOne
  @JoinColumn(name = "pic_id", referencedColumnName = "id")
  private Picture picture;

  @Column(name = "city_id1")
  private long choice1;

  @Column(name = "city_id2")
  private long choice2;

  @Column(name = "city_id3")
  private long choice3;

  public Question(@NonNull UUID id,
                  @NonNull Picture picture,
                  @NonNull Set<Long> choices) {
    requireValidChoices(choices, picture);
    this.id = id.toString();
    this.picture = picture;
    Iterator<Long> iterator = choices.iterator();
    this.choice1 = iterator.next();
    iterator.hasNext();
    this.choice2 = iterator.next();
    iterator.hasNext();
    this.choice3 = iterator.next();
  }

  private void requireValidChoices(Set<Long> choices, Picture picture) {
    if (choices.size() < 3) {
      throw new IllegalArgumentException("Insufficient choices");
    }

    if (choices.size() > 4) {
      throw new IllegalArgumentException("Too many choices");
    }

    if (choices.contains(picture.getCityId())) {
      throw new IllegalArgumentException("picture's city cannot be one of the choices");
    }
  }

  public @NonNull String pictureUrl() {
    return picture.getUrl();
  }

  public boolean isCorrectAnswer(long answerCityId) {
    return picture.isFrom(answerCityId);
  }

  public long answerCityId() {
    return this.picture.getCityId();
  }

  public @NonNull Set<Long> choices() {
    Set<Long> choices = new HashSet<>(4);
    choices.add(picture.getCityId());
    choices.add(choice1);
    choices.add(choice2);
    choices.add(choice3);
    return choices;
  }
}
