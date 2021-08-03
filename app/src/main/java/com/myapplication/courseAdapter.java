package com.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class courseAdapter extends RecyclerView.Adapter<courseAdapter.courseViewHolder> {
    private ArrayList<Course> courseList;
 public class courseViewHolder extends RecyclerView.ViewHolder {
    public TextView courseTitleLabel;
    public TextView startDateLabel;
    public TextView endDateLabel;

     public courseViewHolder(@NonNull View itemView) {
         super(itemView);
         courseTitleLabel = itemView.findViewById(R.id.courseTitleLabel);
         startDateLabel = itemView.findViewById(R.id.startDateLabel);
         endDateLabel = itemView.findViewById(R.id.endDateLabel);
         itemView.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Intent i = new Intent(itemView.getContext(), CourseDetailModify.class);
                 i.putExtra("courseID", String.valueOf(courseList.get(getAdapterPosition()).getCourseID()));
                 i.putExtra("note", String.valueOf(courseList.get(getAdapterPosition()).getNote()));
                 i.putExtra("termID", String.valueOf(courseList.get(getAdapterPosition()).getTermID()));
                 i.putExtra("title", String.valueOf(courseList.get(getAdapterPosition()).getTitle()));
                 i.putExtra("startDate", String.valueOf(courseList.get(getAdapterPosition()).getStartDate()));
                 i.putExtra("endDate", String.valueOf(courseList.get(getAdapterPosition()).getEndDate()));
                 i.putExtra("status", String.valueOf(courseList.get(getAdapterPosition()).getStatus()));
                 i.putExtra("courseMentorID", String.valueOf(courseList.get(getAdapterPosition()).getCourseMentorID()));
                 i.putExtra("startAlert", String.valueOf(courseList.get(getAdapterPosition()).getStartAlert()));
                 i.putExtra("endAlert", String.valueOf(courseList.get(getAdapterPosition()).getEndAlert()));

                 view.getContext().startActivity(i);
             }
         });
     }

 }
    public courseAdapter(ArrayList<Course> tl){
     courseList = tl;

    }
    @NonNull
    @Override
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course,parent,false);
        courseViewHolder cvh = new courseViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull courseViewHolder holder, int position) {
    Course currentCourse = courseList.get(position);
    holder.courseTitleLabel.setText(currentCourse.getTitle());
    holder.startDateLabel.setText(currentCourse.getStartDate());
    holder.endDateLabel.setText(currentCourse.getEndDate());




    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }
}
