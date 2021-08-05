package edu.neu.numad21su.attention.quizScreen;

import java.util.List;

public class QuizEntry {
  String quizEntryId;
  List<QuestionEntry> questionEntries;


  String userId;
  public QuizEntry(String quizEntryId, List<QuestionEntry> questionEntries, String userId) {
    this.quizEntryId = quizEntryId;
    this.questionEntries = questionEntries;
    this.userId = userId;
  }

  public QuizEntry(List<QuestionEntry> questionEntries, String userId) {
    this.questionEntries = questionEntries;
    this.userId = userId;
  }
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
