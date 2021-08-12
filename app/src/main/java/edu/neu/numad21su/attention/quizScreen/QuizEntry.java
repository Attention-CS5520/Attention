package edu.neu.numad21su.attention.quizScreen;

import java.io.Serializable;
import java.util.List;

public class QuizEntry implements Serializable {
  String quizId;
  String quizName;
  String quizEntryId;
  List<QuestionEntry> questionEntries;
  String userId;

  public QuizEntry(String quizId, String quizName, String quizEntryId,
                   List<QuestionEntry> questionEntries, String userId) {
    this.quizId = quizId;
    this.quizName = quizName;
//    this.quizEntryId = quizEntryId;
    this.questionEntries = questionEntries;
    this.userId = userId;
  }

  public QuizEntry(String quizId, String quizEntryId, List<QuestionEntry> questionEntries, String userId) {
    this.quizId = quizId;
    this.quizEntryId = quizEntryId;
    this.questionEntries = questionEntries;
    this.userId = userId;
  }
  public QuizEntry(String quizId, List<QuestionEntry> questionEntries, String userId) {
    this.quizId = quizId;
    this.questionEntries = questionEntries;
    this.userId = userId;
  }

  public QuizEntry(List<QuestionEntry> questionEntries, String userId) {
    this.questionEntries = questionEntries;
    this.userId = userId;
  }

  public String getQuizName() {
    return quizName;
  }

  public void setQuizName(String quizName) {
    this.quizName = quizName;
  }

  public QuizEntry(List<QuestionEntry> questionEntries, String userId, String quizName) {
    this.questionEntries = questionEntries;
    this.userId = userId;
    this.quizName = quizName;
  }
  public QuizEntry(String quizId) {
    this.quizId = quizId;
  }

  public String getQuizId() {return quizId;}

  public String setQuizId(String quizId) {return this.quizId = quizId;}

  public String getQuizEntryId() {
    return quizEntryId;
  }

  public void setQuizEntryId(String quizEntryId) {
    this.quizEntryId = quizEntryId;
  }

  public List<QuestionEntry> getQuestionEntries() {
    return questionEntries;
  }

  public void setQuestionEntries(List<QuestionEntry> questionEntries) {
    this.questionEntries = questionEntries;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }
}
