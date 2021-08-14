package edu.neu.numad21su.attention;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class QuizRecyclerHolder extends RecyclerView.ViewHolder {
  public TextView title;
  public TextView lastEdit;

  public QuizRecyclerHolder(View view, ItemClickListener editItemListener,
                            ItemClickListener deleteItemListener) {
    super(view);
    title = itemView.findViewById(R.id.quiz_manager_title);
    lastEdit = itemView.findViewById(R.id.quiz_last_edit);
    this.addListener(itemView.findViewById(R.id.edit_quiz_icon), editItemListener);
    this.addListener(itemView.findViewById(R.id.delete_quiz_icon),deleteItemListener);
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
