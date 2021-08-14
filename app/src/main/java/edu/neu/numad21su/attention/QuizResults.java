package edu.neu.numad21su.attention;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.github.slugify.Slugify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import edu.neu.numad21su.attention.quizScreen.QuestionEntry;
import edu.neu.numad21su.attention.quizScreen.QuizEntry;


/**
 * QuizResults takes the data file created from taking a Quiz (QuizEntry) and calculates the score onboard
 * the device. QuizResults implements Serialiazable in order to allow transfer of the QuizEntry object
 * between activities.
 */
public class QuizResults extends AppCompatActivity implements Serializable {

  private static final String TAG = "";
  private FirebaseFirestore primaryDB;
  private FirebaseAuth mAuth;
  protected String userFirstName;
  protected String userLastName;
  protected QuizEntry quizEntry;
  FirebaseUser currentUser;
  private RecyclerView recyclerView;
  private QuestionResultsRecyclerAdapter rviewAdapter;
  private RecyclerView.LayoutManager rLayoutManger;
  private List<QuestionEntry> itemList = new ArrayList<>();

  /**
   * In onCreate the main instance is created and the view is opened. The Firebase Databse is retreived
   * from Firestore. The user Authentication is retrieved from Firebase Authentication
   * and the Serializable QuizEntry object is pulled from the previous Quiz activity in order
   * to be able to process and score the quiz.
   * @param savedInstanceState
   */
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz_results);
    primaryDB = FirebaseFirestore.getInstance();
    mAuth = FirebaseAuth.getInstance();
    currentUser = mAuth.getCurrentUser();
    quizEntry = (QuizEntry) getIntent().getSerializableExtra("quizEntry");
    getUserData();
    getQuizData(quizEntry);
    double score = calculateResults(quizEntry) * 100;
    updateDisplay(score, calculateGrade(score));
    int intScore = (int) (score * 7.5);
    updateBars(intScore,150,200,250);
    updatePercentages(score, 66, 0, 100);
    getQuizQuestions(quizEntry);
  }

  public void updatePercentages(double userScore, int classAverage, int low, int high) {
    int intScore = (int) userScore;
    TextView userScorePercent = findViewById(R.id.answer_1_amount);
    userScorePercent.setText("You: " + intScore);
  }

  /**
   * updateBars changes the height of the colored bars on the screen programtically in order to give
   * a visual display of how the user did. The bars are caluclated using a total of 300 points
   * dependent on how the user did on the quiz overall and compared to the class.
   * @param score1 - Score height of the first bar
   * @param score2 - score height of the second bar
   * @param score3 - score height of the third bar
   * @param score4 - score height of the fourth bar
   */
  public void updateBars(int score1, int score2, int score3, int score4) {
    ConstraintLayout layout = findViewById(R.id.quizResults);
    View progressBar1 = layout.findViewById(R.id.progress_bar_1);
    View progressBar2 = layout.findViewById(R.id.progress_bar_2);
    View progressBar3 = layout.findViewById(R.id.progress_bar_3);
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

  /**
   * calculateGrade takes a score from the function calculateResults and produces a letter grade
   * dependent on that score.
   * @param score - The score of the quiz, as a double out of 100
   * @return - A letter grade depending on the score of the quiz
   */
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

  /**
   * updateDisplay updates the View on the device to reflect information about the grades of the quiz
   * @param score - The score of the quiz out of 100
   * @param grade - The letter grade of the quiz based on the score
   */
  public void updateDisplay(double score, String grade) {
    TextView percent = findViewById(R.id.percent);
    TextView letterGrade = findViewById(R.id.letter);
    int truncatedScore = (int) score;
    String percentageScore = String.valueOf(truncatedScore);
    percent.setText("Score: " + truncatedScore + "%");
    letterGrade.setText("Letter Grade: "+ grade);
  }

  /**
   * Fills in the user information at the top of the View to show who's quiz was graded
   */
  public void fillUserInfo() {
    TextView login = findViewById(R.id.user_name);
    String usersName = ("Student: " + userFirstName + " " + userLastName);
    login.setText(usersName);
  }

  /**
   * slugify creates an e-mail slug without any periods to ensure no Firestore DB errors
   */
  private String slugify(String text) {
    Slugify slg = new Slugify();
    String slug = slg.slugify(text);
    return slug;
  }

  /**
   * getUserData takes the Firebase User Authentication and retrieves the First and Last name of the
   * user to use in fillUserInfo()
   */
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

  /**
   * getQuizData takes in a QuizEntry object and fills the data at the top of the view to display what
   * quiz was just taken.
   * @param quiz - a quizEntry object that was created when completing a quiz.
   */
  public void getQuizData(QuizEntry quiz) {
    TextView quizName = findViewById(R.id.quiz_name);
    quizName.setText("Quiz: " +quizEntry.getQuizName());
  }

  /**
   * A method taken from online sources that rounds a double to a certain number of decimal places
   * @param value - The value (Double) to be rounded
   * @param places - How many decimal places to be kept in rounding
   * @return - A double with places number of decimal places
   */
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
      if (currentEntry.getSelectedOption().equals(currentEntry.getQuestionId().correctAnswer)) {
        totalPoints = (totalPoints + 10);
      }
    }
    score = totalPoints / totalPossiblePoints;
    score = round(score, 2);
    return score;
  }

  /**
   * createRecyclerView creates a new RecyclerView to display the question asked on the quiz,
   * and the correct answer to each question. It also displays a check mark or x depending
   * on whether the user go the answer correct or wrong.
   */
  private void createRecyclerView() {
    rLayoutManger = new LinearLayoutManager(this);
    recyclerView = findViewById(R.id.recyclerView);
    recyclerView.setHasFixedSize(true);
    rviewAdapter = new QuestionResultsRecyclerAdapter(itemList);
    recyclerView.setAdapter(rviewAdapter);
    recyclerView.setLayoutManager(rLayoutManger);
  }

  /**
   * getQuizQuestions takes a quizEntry object and adds the questions to a list itemList to be
   * dusplayed in the createRecyclerView later on.
   */
  private void getQuizQuestions(QuizEntry quiz){
    List<QuestionEntry> questionEntries = quiz.getQuestionEntries();
    for (int i = 0; i < questionEntries.size(); i++) {
      QuestionEntry currentEntry = questionEntries.get(i);
      itemList.add(currentEntry);
    }
    createRecyclerView();
  }

}
