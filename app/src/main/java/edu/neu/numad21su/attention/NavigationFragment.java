package edu.neu.numad21su.attention;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavigationFragment#newInstance} factory method to
 * create an instance of this fragment.
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

    private Button handRaiseButton;

    public NavigationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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

        // Connect with firebase
        db = FirebaseFirestore.getInstance();








    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_navigation, container, false);
        handRaiseButton = view.findViewById(R.id.raise_hand_button);
        handRaiseButton.setOnClickListener(this);




        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_navigation, container, false);

        return view;

    }

   @Override
    public void onClick(View v){


       // LiveClassDiscussionActivity activity = (LiveClassDiscussionActivity) getActivity();
        postHandRaiseToDataBase();


    }


    // Put the hand raising code here

    // Subscribe to a hand-raising collection in the database

    // If user is a professor, subscribe to hand raising collection in the firebase database

    // Hook up navigation bar fragment

    // RaiseHand()


    // Listener for a click of the Hand button



    // A method to add a new hand-raising event to the database
    private void postHandRaiseToDataBase(){

        handRaiseButton = handRaiseButton.findViewById(R.id.raise_hand_button);


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
                Toast.makeText(getActivity(), "Hand raised!", Toast.LENGTH_SHORT).show();




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                Log.d("error", e.toString());

            }
        });



    }

    // A method to alert the phone, if the user is registered as an instructor and a new hand raise is posted


}