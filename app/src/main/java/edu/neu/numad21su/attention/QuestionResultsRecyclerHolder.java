package edu.neu.numad21su.attention;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionResultsRecyclerHolder extends RecyclerView.ViewHolder {
    public TextView preview;

    public QuestionResultsRecyclerHolder(View view, ItemClickListener editItemListener, ItemClickListener deleteItemListener) {
        super(view);
        preview = itemView.findViewById(R.id.question_preview);
    }
}
