package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.slugify.Slugify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuestionEntry;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;

public class QuizResults extends AppCompatActivity implements Serializable {

  private static final String TAG = "";
  private FirebaseFirestore primaryDB;
  private FirebaseAuth mAuth;
  protected String userFirstName;
  protected String userLastName;
  protected QuizEntry quizEntry;
  FirebaseUser currentUser;
  private RecyclerView recyclerView;
  private QuestionRecyclerAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private List<Question> itemList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_results);
    primaryDB = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    currentUser = mAuth.getCurrentUser();
    quizEntry = (QuizEntry) getIntent().getSerializableExtra("quizEntry");
    System.out.println(quizEntry);
    getUserData();
    getQuizData(quizEntry);
    double score = calculateResults(quizEntry) * 100;
    updateDisplay(score, calculateGrade(score));
    getQuizQuestions(quizEntry);
  }

  public void updateBars(int score1, int score2, int score3, int score4) {
    ConstraintLayout layout = findViewById(R.id.quizResults);
    View progressBar1 = layout.findViewById(R.id.progress_bar_2);
    View progressBar2 = layout.findViewById(R.id.progress_bar_3);
    View progressBar3 = layout.findViewById(R.id.progress_bar_1);
    View progressBar4 = layout.findViewById(R.id.progress_bar_4);
    ViewGroup.LayoutParams params1 = progressBar1.getLayoutParams();
    ViewGroup.LayoutParams params2 = progressBar2.getLayoutParams();
    ViewGroup.LayoutParams params3 = progressBar3.getLayoutParams();
    ViewGroup.LayoutParams params4 = progressBar4.getLayoutParams();
    params1.height = score1;
    params2.height = score2;
    params3.height = score3;
    params4.height = score4;
    progressBar1.setLayoutParams(params1);
    progressBar2.setLayoutParams(params2);
    progressBar3.setLayoutParams(params3);
    progressBar4.setLayoutParams(params4);
  }

  public String calculateGrade(double score) {
    if (score > 90) {
      return "A";
    } else if (90 > score && score > 79) {
      return "B";
    } else if (80 > score && score < 69) {
      return "C";
    } else if (70 > score && score < 59) {
      return "D";
    } else if (score < 60) {
      return "F";
    } else {
      return "N/A";
    }
  }

  public void updateDisplay(double score, String grade) {
    TextView percent = findViewById(R.id.percent);
    TextView letterGrade = findViewById(R.id.letter);
    int truncatedScore = (int) score;
    String percentageScore = String.valueOf(truncatedScore);
    percent.setText("Score: " + truncatedScore + "%");
    letterGrade.setText("Letter Grade: "+ grade);
  }

  public void fillUserInfo() {
    TextView login = findViewById(R.id.user_name);
    String usersName = ("Student: " + userFirstName + " " + userLastName);
    login.setText(usersName);
  }

  private String slugify(String text) {
    Slugify slg = new Slugify();
    String slug = slg.slugify(text);
    return slug;
  }

  public void getUserData() {
    String email = mAuth.getCurrentUser().getEmail();
    DocumentReference docRef = primaryDB.collection("userType").document(slugify(email));
    docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
      @Override
      public void onComplete(@NonNull Task<DocumentSnapshot> task) {
        if (task.isSuccessful()) {
          DocumentSnapshot document = task.getResult();
          if (document.exists()) {
            userFirstName = document.get("firstName").toString();
            userLastName = document.get("lastName").toString();
            fillUserInfo();
          } else {
            Log.d(TAG, "No such document");
          }
        } else {
          Log.d(TAG, "get failed with ", task.getException());
        }
      }
    });
  }

  public void getQuizData(QuizEntry quiz) {
    TextView quizName = findViewById(R.id.quiz_name);
    quizName.setText("Quiz: " +quizEntry.getQuizName());
  }

  public double round(double value, int places) {
    if (places < 0) throw new IllegalArgumentException();
    long factor = (long) Math.pow(10, places);
    value = value * factor;
    long tmp = Math.round(value);
    return (double) tmp / factor;
  }

  /**
   * Input QuizEntry, Output double (score of the quiz) Takes the quiz object passed from the quizEntry
   * and uses the answers to create a score out of 100. Score is returned as double to be displayed
   * as a percentage
   */
  private double calculateResults(QuizEntry quiz) {
    double totalPoints = 0.0;
    double totalPossiblePoints = quiz.getQuestionEntries().size() * 10;
    double score;
    List<QuestionEntry> questionEntries = quiz.getQuestionEntries();
    for (int i = 0; i < questionEntries.size(); i++) {
      QuestionEntry currentEntry = questionEntries.get(i);
      System.out.println(currentEntry.getSelectedOption());
      System.out.println(currentEntry.getQuestionId().correctAnswer);
      if (currentEntry.getSelectedOption().equals(currentEntry.getQuestionId().correctAnswer)) {
        totalPoints = (totalPoints + 10);
      }
    }
    score = totalPoints / totalPossiblePoints;
    score = round(score, 2);
    return score;
  }

  private void createRecyclerView() {
    rLayoutManger = new LinearLayoutManager(this);
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    rviewAdapter = new QuestionRecyclerAdapter(itemList);
    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);
  }

  private void getQuizQuestions(QuizEntry quiz){
    List<QuestionEntry> questionEntries = quiz.getQuestionEntries();
    for (int i = 0; i < questionEntries.size(); i++) {
      QuestionEntry currentEntry = questionEntries.get(i);
      itemList.add(currentEntry.getQuestionId());
    }
    createRecyclerView();
  }
}
