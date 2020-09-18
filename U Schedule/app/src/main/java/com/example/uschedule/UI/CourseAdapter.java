package com.example.uschedule.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.uschedule.AssessActivity;
import com.example.uschedule.Entities.CourseEntity;
import com.example.uschedule.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private final LayoutInflater mInflater;
    private final Context context;
    private List<CourseEntity> mCourses; // Cached copy of words

    public CourseAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public CourseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.course_list_item, parent, false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CourseViewHolder holder, int position) {
        if (mCourses != null) {
            CourseEntity current = mCourses.get(position);
            holder.courseItemView.setText(current.getCourseName());
            holder.courseItemView2.setText(current.getEndDate());
        } else {
            // Covers the case of data not being ready yet.
            holder.courseItemView.setText("No Word");
            holder.courseItemView2.setText("No Word");
        }

    }

    public void setWords(List<CourseEntity> words) {
        mCourses = words;
        notifyDataSetChanged();
    }

    public CourseEntity getCourseAt(int position) {
        return mCourses.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mCourses != null)
            return mCourses.size();
        else return 0;
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private final TextView courseItemView2;

        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.courseTextView);
            courseItemView2 = itemView.findViewById(R.id.courseTextView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    final CourseEntity current = mCourses.get(position);
                    Intent intent = new Intent(context, AssessActivity.class);
                    intent.putExtra("courseID", current.getCourseID());
                    intent.putExtra("courseName", current.getCourseName());
                    intent.putExtra("courseStart", current.getStartDate());
                    intent.putExtra("courseEnd", current.getEndDate());
                    intent.putExtra("status", current.getStatus());
                    intent.putExtra("mentor", current.getMentor());
                    intent.putExtra("note", current.getNote());
                    intent.putExtra("termID", current.getTermID());
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        }

    }
}