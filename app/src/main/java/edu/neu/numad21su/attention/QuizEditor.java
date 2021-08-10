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
import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

  public void saveQuiz(View view) {
    quiz.setQuizTitle(quizTitle.getText().toString());
    quiz.setLastEdited(new SimpleDateFormat("yyyy-MM-dd H:mm aaa").format(new Date()));
    if(quiz.quizId == null){
      quiz.quizId = UUID.randomUUID().toString();
    }
    db.collection("quizzes").document(quiz.getQuizId()).set(quiz, SetOptions.merge())
            .addOnSuccessListener(dr -> {
              Toast.makeText(QuizEditor.this, "Saved!", Toast.LENGTH_SHORT).show();
              QuizEditor.this.finish();
            }).addOnFailureListener(e -> Log.d("error", e.toString()));
  }
}
