package edu.neu.numad21su.attention;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import edu.neu.numad21su.attention.quizScreen.Question;

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerHolder> {

  private final List<Question> itemList;
  private ItemClickListener deleteItemListener;
  private ItemClickListener editItemListener;

  //Constructor
  public QuestionRecyclerAdapter(List<Question> itemList) {
    this.itemList = itemList;
  }

  public void setEditItemListener(ItemClickListener listener) {
    this.editItemListener = listener;
  }
  public void setDeleteItemListener(ItemClickListener listener) {
    this.deleteItemListener = listener;
  }

  @Override
  public QuestionRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_manager_card, parent, false);
    return new QuestionRecyclerHolder(view, editItemListener, deleteItemListener);
  }

  @Override
  public void onBindViewHolder(QuestionRecyclerHolder holder, int position) {
    Question currentItem = itemList.get(position);
    holder.preview.setText(currentItem.getQuestionText());
  }

  @Override
  public int getItemCount() {
    return itemList.size();
  }
}
