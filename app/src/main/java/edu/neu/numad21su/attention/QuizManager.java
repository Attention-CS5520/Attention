package edu.neu.numad21su.attention;

import android.content.Intent;
import android.os.Bundle;

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
                                                                      "C","D", "C")))),
                  new Quiz("1", "2", "Loops", "1/1/2001", new ArrayList<>()),
                  new Quiz("1", "3", "Memory Management", "12/25/2021", new ArrayList<>()),
                  new Quiz("1", "4", "Recursion", "3/3/2003", new ArrayList<>())
          ));

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_manager);
    createRecyclerView();
  }

  private void createRecyclerView() {
    rLayoutManger = new LinearLayoutManager(this);
    recyclerView = findViewById(R.id.quizCardRecycler);
    recyclerView.setHasFixedSize(true);
    rviewAdapter = new QuizRecyclerAdapter(quizCards);
    rviewAdapter.setDeleteItemListener(i -> {
      quizCards.remove(i);
      rviewAdapter.notifyDataSetChanged();
    });
    rviewAdapter.setEditItemListener(i -> {
      Intent editIntent = new Intent(QuizManager.this, QuizEditor.class);
      editIntent.putExtra("Quiz", quizCards.get(i));
      QuizManager.this.startActivity(editIntent);
    });
    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);
  }
}
