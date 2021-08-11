package edu.neu.numad21su.attention.quizScreen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Quiz implements Serializable {
  public String classId;
  public String quizId;
  public String quizTitle;
  public String lastEdited;
  public List<Question> questions;

  public Quiz() {
    this(null,null,"",null,new ArrayList<>());
  }

  public Quiz(String classId, String quizId, String quizTitle,
              String lastEdited, List<Question> questions) {
    this.classId = classId;
    this.quizId = quizId;
    this.lastEdited = lastEdited;
    this.questions = new ArrayList<>(questions);
    this.quizTitle = quizTitle;
  }

  public String getLastEdited() {
    return lastEdited;
  }

  public void setLastEdited(String lastEdited) {
    this.lastEdited = lastEdited;
  }

  public String getQuizTitle() {
    return quizTitle;
  }

  public void setQuizTitle(String quizTitle) {
    this.quizTitle = quizTitle;
  }

  public String getClassId() {
    return classId;
  }

  public void setClassId(String classId) {
    this.classId = classId;
  }

  public String getQuizId() {
    return quizId;
  }

  public void setQuizId(String quizId) {
    this.quizId = quizId;
  }

  public List<Question> getQuestions() {
    return questions;
  }

  public void setQuestions(List<Question> questions) {
    this.questions = questions;
  }
}
