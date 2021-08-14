package edu.neu.numad21su.attention.quizmanager;

import com.google.firebase.firestore.FirebaseFirestore;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import edu.neu.numad21su.attention.R;
import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class QuestionEditor extends AppCompatActivity {

  private Question question;
  private EditText questionText;
  private EditText optionA;
  private EditText optionB;
  private EditText optionC;
  private EditText optionD;
  private RadioGroup radioGroup;
  private FirebaseFirestore db;
  private Quiz quiz;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_question_editor);
    questionText = findViewById(R.id.question_editor_question_text);
    optionA = findViewById(R.id.question_editor_optionA);
    optionB = findViewById(R.id.question_editor_optionB);
    optionC = findViewById(R.id.question_editor_optionC);
    optionD = findViewById(R.id.question_editor_optionD);
    radioGroup = findViewById(R.id.question_answer_radio_group);
    db = FirebaseFirestore.getInstance();
    this.question = (Question) this.getIntent().getSerializableExtra("Question");
    this.quiz = (Quiz) this.getIntent().getSerializableExtra("QuizId");
    if (question != null) {
      questionText.setText(question.getQuestionText());
      optionA.setText(question.getOptionA());
      optionB.setText(question.getOptionB());
      optionC.setText(question.getOptionC());
      optionD.setText(question.getOptionD());
    }
  }

  public void saveQuestion(View view) {
    if (question.correctAnswer != null) {
      question.setQuestionText(questionText.getText().toString());
      question.setOptionA(optionA.getText().toString());
      question.setOptionB(optionB.getText().toString());
      question.setOptionC(optionC.getText().toString());
      question.setOptionD(optionD.getText().toString());
      if (question.questionId == null) {
        question.questionId = UUID.randomUUID().toString();
        quiz.questions.add(question);
      } else {
        quiz.questions.replaceAll(q -> q.questionId.equals(question.questionId) ? question : q);
      }
      db.collection("quizzes").document(quiz.quizId)
              .update("questions", quiz.questions)
              .addOnSuccessListener(dr -> {
                Toast.makeText(QuestionEditor.this, "Question Saved!", Toast.LENGTH_SHORT).show();
                finish();
              })
              .addOnFailureListener(e -> Log.d("error", e.toString()));
    } else {
      Toast.makeText(QuestionEditor.this, "Please select an answer first.", Toast.LENGTH_SHORT).show();
    }
  }

  public void selectAnswer(View view) {
    switch (view.getId()) {
      case R.id.answerA_selected:
        question.correctAnswer = "A";
        break;
      case R.id.answerB_selected:
        question.correctAnswer = "B";
        break;
      case R.id.answerC_selected:
        question.correctAnswer = "C";
        break;
      case R.id.answerD_selected:
        question.correctAnswer = "D";
        break;
    }
  }
}
