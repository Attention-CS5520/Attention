package edu.neu.numad21su.attention.quizmanager;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.ItemClickListener;
import edu.neu.numad21su.attention.R;

public class QuestionRecyclerHolder extends RecyclerView.ViewHolder {
  public TextView preview;

  public QuestionRecyclerHolder(View view, ItemClickListener editItemListener, ItemClickListener deleteItemListener) {
    super(view);
    preview = itemView.findViewById(R.id.question_preview);
    this.addListener(itemView.findViewById(R.id.edit_question_icon), editItemListener);
    this.addListener(itemView.findViewById(R.id.delete_question_icon),deleteItemListener);
  }

  private void addListener(View view, ItemClickListener listener) {
    view.setOnClickListener(v -> {
      if (listener != null) {
        int position = getLayoutPosition();
        if (position != RecyclerView.NO_POSITION) {
          listener.onItemClick(position);
        }
      }
    });
  }
}
