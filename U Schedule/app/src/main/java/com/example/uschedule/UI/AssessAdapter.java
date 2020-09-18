package com.example.uschedule.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uschedule.AssessDetail;
import com.example.uschedule.Entities.AssessEntity;
import com.example.uschedule.R;

import java.util.List;

public class AssessAdapter extends RecyclerView.Adapter<AssessAdapter.AssessViewHolder>{
    private final LayoutInflater mInflater;
    private final Context context;
    private List<AssessEntity> mAssess; // Cached copy of words

    public AssessAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public AssessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.assess_list_item, parent, false);

        return new AssessViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AssessViewHolder holder, int position) {
        if (mAssess != null) {
            AssessEntity current = mAssess.get(position);
            holder.assessItemView.setText(current.getAssessName());
            holder.assessItemView2.setText(current.getDueDate());
        } else {
            // Covers the case of data not being ready yet.
            holder.assessItemView.setText("No Word");
            holder.assessItemView2.setText("No Word");
        }

    }

    public void setWords(List<AssessEntity> words) {
        mAssess = words;
        notifyDataSetChanged();
    }

    public AssessEntity getAssessAt(int position) {
        return mAssess.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mAssess != null)
            return mAssess.size();
        else return 0;
    }

    class AssessViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessItemView;
        private final TextView assessItemView2;


        private AssessViewHolder(View itemView) {
            super(itemView);
            assessItemView = itemView.findViewById(R.id.assessTextView);
            assessItemView2 = itemView.findViewById(R.id.assessTextView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final AssessEntity current = mAssess.get(position);
                    Intent intent = new Intent(context, AssessDetail.class);
                    intent.putExtra("assessID", current.getAssessID());
                    intent.putExtra("assessName", current.getAssessName());
                    intent.putExtra("assessType", current.getType());
                    intent.putExtra("assessDue", current.getDueDate());
                    intent.putExtra("courseID", current.getCourseID());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }

    }
}
