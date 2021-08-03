package edu.neu.numad21su.attention.quizScreen;

public class QuestionEntry {
  String questionEntryId;
  String questionId;
  public enum SelectedOption {OPTION_A,OPTION_B,OPTION_C,OPTION_D}
  SelectedOption selectedOption;

  public QuestionEntry(String questionEntryId, String questionId, SelectedOption selectedOption) {
    this.questionEntryId = questionEntryId;
    this.questionId = questionId;
    this.selectedOption = selectedOption;
  }


  public String getQuestionEntryId() {
    return questionEntryId;
  }

  public void setQuestionEntryId(String questionEntryId) {
    this.questionEntryId = questionEntryId;
  }

  public String getQuestionId() {
    return questionId;
  }

  public void setQuestionId(String questionId) {
    this.questionId = questionId;
  }
}
