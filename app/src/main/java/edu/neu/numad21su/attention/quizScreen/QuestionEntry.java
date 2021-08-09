package edu.neu.numad21su.attention.quizScreen;

public class QuestionEntry {
  String questionEntryId;
  String questionId;
  String selectedOption;

  public String getSelectedOption() {
    return selectedOption;
  }

  public void setSelectedOption(String selectedOption) {
    this.selectedOption = selectedOption;
  }

  public QuestionEntry(String questionEntryId, String questionId, String selectedOption) {
    this.questionEntryId = questionEntryId;
    this.questionId = questionId;
    this.selectedOption = selectedOption;
  }

  public QuestionEntry(String questionId, String selectedOption) {
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
