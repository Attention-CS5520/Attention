package edu.neu.numad21su.attention;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.quizScreen.Quiz;

public class QuizRecyclerAdapter extends RecyclerView.Adapter<QuizRecyclerHolder> {

  private final List<Quiz> itemList;
  private ItemClickListener deleteItemListener;
  private ItemClickListener editItemListener;

  //Constructor
  public QuizRecyclerAdapter(List<Quiz> itemList) {
    this.itemList = itemList;
  }

  public void setEditItemListener(ItemClickListener listener) {
    this.editItemListener = listener;
  }
  public void setDeleteItemListener(ItemClickListener listener) {
    this.deleteItemListener = listener;
  }

  @Override
  public QuizRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_manager_card, parent, false);
    return new QuizRecyclerHolder(view, editItemListener, deleteItemListener);
  }

  @Override
  public void onBindViewHolder(QuizRecyclerHolder holder, int position) {
    Quiz currentItem = itemList.get(position);
    holder.title.setText(currentItem.getQuizTitle());
    holder.lastEdit.setText(currentItem.getLastEdited());
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }
}
