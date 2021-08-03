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

public class subAssessmentAdapter extends RecyclerView.Adapter<subAssessmentAdapter.assessmentViewHolder> {
    static private ArrayList<Assessment> assessmentList;
    static ArrayList<Integer> assessmentsChecked = new ArrayList<>();
    static ArrayList<Integer> assessmentsPreviousChecked = new ArrayList<>();
    static public int courseID;

 public static class assessmentViewHolder extends RecyclerView.ViewHolder {

     public TextView type;
     public TextView name;
     public SwitchCompat assessmentSublistToggle;
     public ImageView checkmark;
     public assessmentViewHolder(@NonNull View itemView) {
         super(itemView);
         type = itemView.findViewById(R.id.assessmentTypeLabel);
         name = itemView.findViewById(R.id.assessmentNameLabel);
         assessmentSublistToggle = itemView.findViewById(R.id.assessmentSublistToggle);
         checkmark = itemView.findViewById(R.id.checkmark);

         itemView.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Assessment currentAssessment = assessmentList.get(getAdapterPosition());
                 ImageView checkMark = itemView.findViewById(R.id.checkmark);
                 SwitchCompat assessmentSublistToggle = itemView.findViewById(R.id.assessmentSublistToggle);
                 if(checkMark.getVisibility() == View.INVISIBLE) {
                     checkMark.setVisibility(View.VISIBLE);
                     assessmentSublistToggle.setChecked(true);
                     if (!assessmentsChecked.contains(currentAssessment.getAssessmentID())) {
                         assessmentsChecked.add(currentAssessment.getAssessmentID());
                         Collections.sort(assessmentsChecked);
                     }
                 }else {
                     checkMark.setVisibility(View.INVISIBLE);
                     assessmentSublistToggle.setChecked(false);
                     if (assessmentsChecked.contains(currentAssessment.getAssessmentID())) {
                         assessmentsChecked.removeAll(Arrays.asList(currentAssessment.getAssessmentID()));
                         Collections.sort(assessmentsChecked);
                     }
                 }

             }

         });

     }

 }
    public subAssessmentAdapter(ArrayList<Assessment> cl, int course_ID){
     assessmentList = cl;
     courseID = course_ID;
    }
    @NonNull
    @Override
    public assessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subitem_assessment,parent,false);
        assessmentViewHolder cvh = new assessmentViewHolder(view);
        return cvh;
    }

    @Override
    public void onBindViewHolder(@NonNull assessmentViewHolder holder, int position) {
    Assessment currentAssessment = assessmentList.get(position);
    holder.type.setText(currentAssessment.getAssessmentType());
    holder.name.setText(currentAssessment.getTitle());
        if (currentAssessment.getCourseID() == courseID) {

            holder.checkmark.setVisibility(View.VISIBLE);
            holder.assessmentSublistToggle.setChecked(true);
            assessmentsChecked.add(currentAssessment.getAssessmentID());
            assessmentsPreviousChecked.add(currentAssessment.getAssessmentID());
            Collections.sort(assessmentsChecked);
            Collections.sort(assessmentsPreviousChecked);
            Log.d("Checked", String.valueOf(currentAssessment.getAssessmentID()));
        } else {
            holder.checkmark.setVisibility(View.INVISIBLE);
            holder.assessmentSublistToggle.setChecked(false);
            Log.d("Not Checked", String.valueOf(currentAssessment.getAssessmentID()));
        }
    }


    @Override
    public int getItemCount() {
        return assessmentList.size();
    }





}

