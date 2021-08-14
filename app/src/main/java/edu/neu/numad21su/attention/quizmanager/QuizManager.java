package edu.neu.numad21su.attention.quizmanager;

import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.R;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class QuizManager extends AppCompatActivity {
  private RecyclerView recyclerView;
  private QuizRecyclerAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private List<Quiz> quizCards = new ArrayList<>();
  private FirebaseFirestore db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_manager);
    createRecyclerView();
    db = FirebaseFirestore.getInstance();
    getQuizzes();
  }

  private void createRecyclerView() {
    rLayoutManger = new LinearLayoutManager(this);
    recyclerView = findViewById(R.id.quizCardRecycler);
    recyclerView.setHasFixedSize(true);
    rviewAdapter = new QuizRecyclerAdapter(quizCards);
    rviewAdapter.setDeleteItemListener(i -> deleteQuiz(quizCards.get(i)));
    rviewAdapter.setEditItemListener(i -> openQuiz(quizCards.get(i)));
    rviewAdapter.setStartQuizListener(i -> startQuiz(quizCards.get(i)));
    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);
  }


  @Override
  protected void onResume() {
    super.onResume();
    getQuizzes();
  }

  private void getQuizzes() {
    db.collection("quizzes").get()
            .addOnSuccessListener(dr -> {
              quizCards.clear();
              quizCards.addAll(dr.toObjects(Quiz.class));
              rviewAdapter.notifyDataSetChanged();
            })
            .addOnFailureListener(e -> Log.d("error", e.toString()));
  }

  private void openQuiz(Quiz quiz) {
    Intent editIntent = new Intent(QuizManager.this, QuizEditor.class);
    editIntent.putExtra("Quiz", quiz);
    QuizManager.this.startActivity(editIntent);
  }

  public void createQuiz(View view) {
    Quiz quiz = new Quiz();
    quizCards.add(quiz);
    openQuiz(quiz);
  }

  private void deleteQuiz(Quiz quiz) {
    db.collection("quizzes").document(quiz.quizId).delete()
            .addOnSuccessListener(dr -> {
              quizCards.remove(quiz);
              rviewAdapter.notifyDataSetChanged();
              Toast.makeText(QuizManager.this, "Deleted.", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> Log.d("error", e.toString()));
  }


  private void startQuiz(Quiz quiz) {
    quiz.startedAtMillis = System.currentTimeMillis();
    db.collection("quizToTake").document(quiz.quizId).set(quiz);
  }
}
