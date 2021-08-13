package edu.neu.numad21su.attention.quizScreen;

import java.io.Serializable;

public class QuestionEntry implements Serializable{
  String questionEntryId;
  String selectedOption;
  Question questionId;

  public String getSelectedOption() {
    return selectedOption;
  }

  public void setSelectedOption(String selectedOption) {
    this.selectedOption = selectedOption;
  }

  public QuestionEntry(String questionEntryId, Question questionId, String selectedOption) {
    this.questionEntryId = questionEntryId;
    this.questionId = questionId;
    this.selectedOption = selectedOption;
  }

  public QuestionEntry(Question questionId, String selectedOption) {
    this.questionId = questionId;
    this.selectedOption = selectedOption;
  }



  public String getQuestionEntryId() {
    return questionEntryId;
  }

  public void setQuestionEntryId(String questionEntryId) {
    this.questionEntryId = questionEntryId;
  }

  public Question getQuestionId() {
    return questionId;
  }

public void setQuestionId(Question questionId) {
    this.questionId = questionId;
  }
}
