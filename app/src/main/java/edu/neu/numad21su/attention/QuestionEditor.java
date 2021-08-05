package edu.neu.numad21su.attention;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import edu.neu.numad21su.attention.quizScreen.Question;

public class QuestionEditor extends AppCompatActivity {

  private Question question;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_question_editor);
    this.question = (Question) this.getIntent().getSerializableExtra("Question");
    if (question != null) {
      ((EditText) findViewById(R.id.question_editor_question_text)).setText(question.getQuestionText());
      ((EditText) findViewById(R.id.question_editor_optionA)).setText(question.getOptionA());
      ((EditText) findViewById(R.id.question_editor_optionB)).setText(question.getOptionB());
      ((EditText) findViewById(R.id.question_editor_optionC)).setText(question.getOptionC());
      ((EditText) findViewById(R.id.question_editor_optionD)).setText(question.getOptionD());
    }
  }
}
