package com.example.uschedule.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uschedule.CourseActivity;
import com.example.uschedule.Entities.TermEntity;
import com.example.uschedule.R;

import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<TermEntity> mTerms; // Cached copy of words
    private Button deleteButton;
    public TermAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public TermViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TermViewHolder holder, int position) {


        if (mTerms != null) {
            final TermEntity current = mTerms.get(position);
            holder.termItemView.setText(current.getTermName());
            holder.termEndDate.setText(current.getEndDate());

        } else {
            // Covers the case of data not being ready yet.
            holder.termItemView.setText("No Word");
            holder.termEndDate.setText("No Date");
        }

    }

    public void delete(int position) {
        mTerms.remove(position);
        notifyItemRemoved(position);
    }

    public void setWords(List<TermEntity> terms) {
        mTerms = terms;
        notifyDataSetChanged();
    }

    public TermEntity getTermAt(int position) {
        return mTerms.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mTerms != null)
            return mTerms.size();
        else return 0;
    }

    class TermViewHolder extends RecyclerView.ViewHolder{
        private final TextView termItemView;
        private final TextView termEndDate;

        private TermViewHolder(View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.termTextView);
            termEndDate = itemView.findViewById(R.id.termEndDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final TermEntity current = mTerms.get(position);
                    Intent intent = new Intent(context, CourseActivity.class);
                    intent.putExtra("termID", current.getTermID());
                    intent.putExtra("termName", current.getTermName());
                    intent.putExtra("termStart", current.getStartDate());
                    intent.putExtra("termEnd", current.getEndDate());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });

        }

    }

}