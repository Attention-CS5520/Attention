package edu.neu.numad21su.attention;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class QuizEditor extends AppCompatActivity {
  private RecyclerView recyclerView;
  private QuestionRecyclerAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private Quiz quiz;
  private FirebaseFirestore db;
  private EditText quizTitle;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_editor);
    this.quiz = (Quiz) this.getIntent().getSerializableExtra("Quiz");
    if (quiz != null) {
      ((TextView) this.findViewById(R.id.quiz_editor_title)).setText(quiz.getQuizTitle());
      ((TextView) this.findViewById(R.id.quiz_editor_last_edited)).setText(quiz.getLastEdited());
    }
    db = FirebaseFirestore.getInstance();
    createRecyclerView();
    quizTitle = findViewById(R.id.quiz_editor_title);
  }

  private void createRecyclerView() {
    rLayoutManger = new LinearLayoutManager(this);
    recyclerView = findViewById(R.id.questionCardRecycler);
    recyclerView.setHasFixedSize(true);
    rviewAdapter = new QuestionRecyclerAdapter(quiz.questions);
    rviewAdapter.setDeleteItemListener(i -> {
      deleteQuiz(quiz.questions.get(i));
      rviewAdapter.notifyDataSetChanged();
    });
    rviewAdapter.setEditItemListener(i -> openQuestion(quiz.questions.get(i)));
    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);
  }

  public void saveQuiz(View view) {
    quiz.setQuizTitle(quizTitle.getText().toString());
    quiz.setLastEdited(new SimpleDateFormat("yyyy-MM-dd H:mm aaa").format(new Date()));
    if (quiz.quizId == null) {
      quiz.quizId = UUID.randomUUID().toString();
    }
    db.collection("quizzes").document(quiz.getQuizId()).set(quiz, SetOptions.merge())
            .addOnSuccessListener(dr -> {
              Toast.makeText(QuizEditor.this, "Saved!", Toast.LENGTH_SHORT).show();
              QuizEditor.this.finish();
            }).addOnFailureListener(e -> Log.d("error", e.toString()));
  }

  public void createQuestion(View view) {
    openQuestion(new Question());
  }

  private void deleteQuiz(Question question) {
    List<Question> questions =
            quiz.questions.stream().filter(q -> !q.equals(question)).collect(Collectors.toList());
    quiz.questions.remove(question);
    db.collection("quizzes").document(quiz.quizId).update("questions", questions)
            .addOnSuccessListener(dr -> {
              quiz.questions.remove(question);
              rviewAdapter.notifyDataSetChanged();
              Toast.makeText(QuizEditor.this, "Question Deleted.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Log.d("error", e.toString()));
  }

  private void openQuestion(Question question) {
    Intent editIntent = new Intent(QuizEditor.this, QuestionEditor.class);
    editIntent.putExtra("Question", question);
    editIntent.putExtra("QuizId", quiz);
    QuizEditor.this.startActivity(editIntent);
  }

  @Override
  protected void onResume() {
    super.onResume();
    refreshQuestions();
  }

  private void refreshQuestions() {
    db.collection("quizzes").document(quiz.getQuizId()).get()
            .addOnSuccessListener(dr -> {
              quiz.questions.clear();
              quiz.questions.addAll(Objects.requireNonNull(dr.toObject(Quiz.class)).questions);
              rviewAdapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> Log.d("error", e.toString()));
  }


}
