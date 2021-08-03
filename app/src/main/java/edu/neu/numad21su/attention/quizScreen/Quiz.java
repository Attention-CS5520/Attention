package edu.neu.numad21su.attention.quizScreen;

import java.util.List;

public class Quiz {
  String classId;
  String quizId;
  List<Question> questions;

  public Quiz(String classId, String quizId, List<Question> questions) {
    this.classId = classId;
    this.quizId = quizId;
    this.questions = questions;
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
