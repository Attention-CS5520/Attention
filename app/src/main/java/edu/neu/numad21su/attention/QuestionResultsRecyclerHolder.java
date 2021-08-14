package edu.neu.numad21su.attention;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionResultsRecyclerHolder extends RecyclerView.ViewHolder {
    public TextView preview;
    public TextView answer;
    public ImageView incorrect;
    public ImageView correct;

    public QuestionResultsRecyclerHolder(View view) {
        super(view);
        preview = itemView.findViewById(R.id.quizResults_question_text);
        answer = itemView.findViewById(R.id.quizResults_answer);
        incorrect = itemView.findViewById(R.id.incorrect_indicator);
        correct = itemView.findViewById(R.id.correct_indicator);
    }
}
