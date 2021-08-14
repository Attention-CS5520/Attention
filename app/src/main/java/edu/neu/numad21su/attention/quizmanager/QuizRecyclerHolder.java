package edu.neu.numad21su.attention.quizmanager;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.ItemClickListener;
import edu.neu.numad21su.attention.R;

public class QuizRecyclerHolder extends RecyclerView.ViewHolder {
  public TextView title;
  public TextView lastEdit;
  private ItemClickListener startQuizListener;

  public QuizRecyclerHolder(View view, ItemClickListener editItemListener,
                            ItemClickListener deleteItemListener,
                            ItemClickListener startQuizListener) {
    super(view);
    title = itemView.findViewById(R.id.quizResults_question_text);
    lastEdit = itemView.findViewById(R.id.quizResults_answer);
    this.addListener(itemView.findViewById(R.id.edit_quiz_icon), editItemListener);
    this.addListener(itemView.findViewById(R.id.delete_quiz_icon),deleteItemListener);
    this.addListener(itemView.findViewById(R.id.start_quiz_icon), startQuizListener);
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
