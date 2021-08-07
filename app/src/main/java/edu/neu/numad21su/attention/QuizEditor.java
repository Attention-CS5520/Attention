package edu.neu.numad21su.attention;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.quizScreen.Quiz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class QuizEditor extends AppCompatActivity {
  private RecyclerView recyclerView;
  private QuestionRecyclerAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private Quiz quiz;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_editor);
    this.quiz = (Quiz) this.getIntent().getSerializableExtra("Quiz");
    if(quiz != null){
      ((TextView)this.findViewById(R.id.quiz_editor_title)).setText(quiz.getQuizTitle());
      ((TextView)this.findViewById(R.id.quiz_editor_last_edited)).setText(quiz.getLastEdited());
    }
    createRecyclerView();
  }

  private void createRecyclerView() {
    rLayoutManger = new LinearLayoutManager(this);
    recyclerView = findViewById(R.id.questionCardRecycler);
    recyclerView.setHasFixedSize(true);
    rviewAdapter = new QuestionRecyclerAdapter(quiz.questions);
    rviewAdapter.setDeleteItemListener(i -> {
      quiz.questions.remove(i);
      rviewAdapter.notifyDataSetChanged();
    });
    rviewAdapter.setEditItemListener(i -> {
      Intent editIntent = new Intent(QuizEditor.this, QuestionEditor.class);
      editIntent.putExtra("Question", quiz.questions.get(i));
      QuizEditor.this.startActivity(editIntent);
    });
    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);
  }
}
