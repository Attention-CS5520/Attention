package edu.neu.numad21su.attention;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.quizScreen.Question;
import edu.neu.numad21su.attention.quizScreen.QuestionEntry;

public class QuestionResultsRecyclerAdapter extends RecyclerView.Adapter<QuestionResultsRecyclerHolder> {

    private final List<QuestionEntry> itemList;

    //Constructor
    public QuestionResultsRecyclerAdapter(List<QuestionEntry> itemList) {
        this.itemList = itemList;
    }

    @Override
    public QuestionResultsRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_results_card, parent, false);
        return new QuestionResultsRecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionResultsRecyclerHolder holder, int position) {
        QuestionEntry currentItem = itemList.get(position);
        holder.preview.setText(currentItem.getQuestionId().getQuestionText());
        holder.answer.setText(currentItem.getQuestionId().getCorrectAnswer());
        if (currentItem.getSelectedOption().equals(currentItem.getQuestionId().getCorrectAnswer())){
            holder.incorrect.setVisibility(View.INVISIBLE);
        } else {
            holder.correct.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}

