package com.myapplication;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class subCourseAdapter extends RecyclerView.Adapter<subCourseAdapter.courseViewHolder> {
    static private ArrayList<Course> courseList;
    static ArrayList<Integer> coursesChecked = new ArrayList<>();
    static ArrayList<Integer> coursesPreviousChecked = new ArrayList<>();
    static public int termID;
 public static class courseViewHolder extends RecyclerView.ViewHolder {

    public TextView name;
     public SwitchCompat courseSublistToggle;
     public ImageView checkmark;
     public courseViewHolder(@NonNull View itemView) {
         super(itemView);
         name = itemView.findViewById(R.id.courseTitleLabel);
         courseSublistToggle = itemView.findViewById(R.id.courseSublistToggle);
         checkmark = itemView.findViewById(R.id.checkmark);
         itemView.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Course currentCourse = courseList.get(getAdapterPosition());
                 ImageView checkMark = itemView.findViewById(R.id.checkmark);
                 SwitchCompat courseSublistToggle = itemView.findViewById(R.id.courseSublistToggle);
                 if(checkMark.getVisibility() == View.INVISIBLE) {
                     checkMark.setVisibility(View.VISIBLE);
                     courseSublistToggle.setChecked(true);
                     if (!coursesChecked.contains(currentCourse.getCourseID())) {
                         coursesChecked.add(currentCourse.getCourseID());
                         Collections.sort(coursesChecked);
                     }
                 }else {
                     checkMark.setVisibility(View.INVISIBLE);
                     courseSublistToggle.setChecked(false);
                     if (coursesChecked.contains(currentCourse.getCourseID())) {
                         coursesChecked.removeAll(Arrays.asList(currentCourse.getCourseID()));
                         Collections.sort(coursesChecked);
                     }
                 }
             }

         });

     }

 }
    public subCourseAdapter(ArrayList<Course> cl,int term_ID){
     courseList = cl;
        termID = term_ID;
    }
    @NonNull
    @Override
    public courseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subitem_course,parent,false);
        courseViewHolder cvh = new courseViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull courseViewHolder holder, int position) {
    Course currentCourse = courseList.get(position);
    holder.name.setText(currentCourse.getTitle());
        if (currentCourse.getTermID() == termID) {

            holder.checkmark.setVisibility(View.VISIBLE);
            holder.courseSublistToggle.setChecked(true);
            coursesChecked.add(currentCourse.getCourseID());
            coursesPreviousChecked.add(currentCourse.getCourseID());
            Collections.sort(coursesChecked);
            Collections.sort(coursesPreviousChecked);
            Log.d("Checked", String.valueOf(currentCourse.getCourseID()));
        } else {
            holder.checkmark.setVisibility(View.INVISIBLE);
            holder.courseSublistToggle.setChecked(false);
            Log.d("Not Checked", String.valueOf(currentCourse.getCourseID()));
        }
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }


}

