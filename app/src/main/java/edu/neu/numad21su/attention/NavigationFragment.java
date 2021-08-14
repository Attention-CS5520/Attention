package edu.neu.numad21su.attention;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.slugify.Slugify;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import edu.neu.numad21su.attention.quizmanager.QuizManager;


/**
 * A simple {@link Fragment} subclass. Use the {@link NavigationFragment#newInstance} factory method
 * to create an instance of this fragment.
 */
public class NavigationFragment extends Fragment implements View.OnClickListener {

  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private FirebaseFirestore db;
  private FirebaseAuth mAuth;

  private Button handRaiseButton;
  private Button backButton;
  private Button pollsQuizButton;
  private FirebaseUser user;
  private FragmentActivity activity;
  private String type;
  private CollectionReference handsRaised;

  public NavigationFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of this fragment using the provided
   * parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment NavigationFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static NavigationFragment newInstance(String param1, String param2) {
    NavigationFragment fragment = new NavigationFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
    this.activity = getActivity();
    // Connect with firebase
    db = FirebaseFirestore.getInstance();

    mAuth = FirebaseAuth.getInstance();
    // Adding quizToTake listener for students only
    user = mAuth.getCurrentUser();
    if (user != null) {
      String emailSlug = new Slugify().slugify(user.getEmail());
      db.collection("userType").document(emailSlug).get().addOnSuccessListener(dr -> {
        type = (String) dr.get("type");
        if (type != null && type.equals("instructor")) {
          Button managerButton = activity.findViewById(R.id.quiz_manager_button);
          managerButton.setVisibility(View.VISIBLE);
        }
      });
    }
    handsRaised = db.collection("hands_raised");
    handsRaised.addSnapshotListener((value, error) -> {
      if (type != null && type.equals("instructor")) {
        Toast.makeText(activity, "A student raised their hand", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {


    View view = inflater.inflate(R.layout.fragment_navigation, container, false);
    handRaiseButton = view.findViewById(R.id.raise_hand_button);
    handRaiseButton.setOnClickListener(this);

    backButton = view.findViewById(R.id.back_button);
    backButton.setOnClickListener(this);

    pollsQuizButton = view.findViewById(R.id.quiz_manager_button);
    pollsQuizButton.setOnClickListener(this);


    // Inflate the layout for this fragment
    // return inflater.inflate(R.layout.fragment_navigation, container, false);

    return view;

  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {

      case R.id.raise_hand_button:
        postHandRaiseToDataBase();
        break;

      case R.id.back_button:
        activity.finish();
        break;

      case R.id.quiz_manager_button:
        Intent switchActivityIntent = new Intent(activity, QuizManager.class);
        startActivity(switchActivityIntent);
        break;
    }

  }


  // A method to add a new hand-raising event to the database
  private void postHandRaiseToDataBase() {

    handRaiseButton = handRaiseButton.findViewById(R.id.raise_hand_button);


    // Catches a null email case
    try {
      String user_email = mAuth.getCurrentUser().getEmail();

    } catch (Exception e) {
      Random random = new Random();


      Date dNow = new Date();
      SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd H:mm aaa");


      Map<String, Object> newHandRaise = new HashMap<>();
      newHandRaise.put("author", "James Harlowe");
      newHandRaise.put("classId", "classId");
      newHandRaise.put("date", ft.format(dNow));

      handsRaised.document("user_hand" + random)
              .set(newHandRaise).addOnSuccessListener(new OnSuccessListener<Void>() {
        @Override
        public void onSuccess(@NonNull Void unused) {
          Log.d("hand raise", "new hand raised");
        }
      }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
          Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
          Log.d("error", e.toString());

        }
      });
      return;

    }


    Random random = new Random();


    Date dNow = new Date();
    SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd H:mm aaa");


    Map<String, Object> newHandRaise = new HashMap<>();
    newHandRaise.put("author", "James Harlowe");
    newHandRaise.put("classId", "classId");
    newHandRaise.put("date", ft.format(dNow));

    db.collection("hands_raised").document("user_hand" + random)
            .set(newHandRaise).addOnSuccessListener(new OnSuccessListener<Void>() {
      @Override
      public void onSuccess(@NonNull Void unused) {
        Log.d("hand raise", "new hand raised");
      }
    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {
        Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
        Log.d("error", e.toString());

      }
    });

    // If the user's email is registered as an instructor, send a toast message:

    // Get the collection of emails
    Query email_addresses = db.collection("userType");

    // Get the collection of hands raised
    Query hands_raised = db.collection("hands_raised");


    email_addresses.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
      @Override
      public void onComplete(@NonNull Task<QuerySnapshot> task) {

        if (task.isSuccessful()) {


          QuerySnapshot querySnapshot = task.getResult();


          for (int i = 0; i < querySnapshot.size(); i++) {

            String account = querySnapshot.getDocuments().get(i).getId();
            String accountType = (String) querySnapshot.getDocuments().get(i).get("type");


            String user_email = mAuth.getCurrentUser().getEmail();


            Slugify slg = new Slugify();
            String emailSlug = slg.slugify(user_email);

            if (account.equals(emailSlug) && accountType.equals("instructor")) {
              Log.d("user type", "current user is instructor");
            }

            if (account.equals(emailSlug) && accountType.equals("student")) {
              Log.d("user type", "current user is student");
            }
          }
        }
      }


    }).addOnFailureListener(new OnFailureListener() {
      @Override
      public void onFailure(@NonNull Exception e) {

        Log.d("discussions", "discussion history not found");

      }
    });


  }


}
