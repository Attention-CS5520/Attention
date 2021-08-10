package edu.neu.numad21su.attention;

import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class QuizManager extends AppCompatActivity {
  private RecyclerView recyclerView;
  private QuizRecyclerAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private List<Quiz> quizCards = new ArrayList<>(
          Arrays.asList(
                  new Quiz("1", "1", "Pointers versus Variables", "10/10/2020",
                           new ArrayList<>(Arrays.asList(new Question("Pick One", "A", "B",
                                                                      "C", "D", "C")))),
                  new Quiz("1", "2", "Loops", "1/1/2001", new ArrayList<>()),
                  new Quiz("1", "3", "Memory Management", "12/25/2021", new ArrayList<>()),
                  new Quiz("1", "4", "Recursion", "3/3/2003", new ArrayList<>())
          ));
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
}
