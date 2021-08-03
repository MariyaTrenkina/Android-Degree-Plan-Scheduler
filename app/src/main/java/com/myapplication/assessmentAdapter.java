package com.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class assessmentAdapter extends RecyclerView.Adapter<assessmentAdapter.assessmentViewHolder> {
    private ArrayList<Assessment> assessmentList;
 public class assessmentViewHolder extends RecyclerView.ViewHolder {
    public TextView assessmentTitleLabel;
    public TextView startDateLabel;
    public TextView endDateLabel;

     public assessmentViewHolder(@NonNull View itemView) {
         super(itemView);
         assessmentTitleLabel = itemView.findViewById(R.id.assessmentTitleLabel);
         startDateLabel = itemView.findViewById(R.id.startDateLabel);
         endDateLabel = itemView.findViewById(R.id.endDateLabel);
         itemView.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Intent i = new Intent(itemView.getContext(), AssessmentDetailModify.class);
                 i.putExtra("assessmentID", String.valueOf(assessmentList.get(getAdapterPosition()).getAssessmentID()));
                 i.putExtra("courseID", String.valueOf(assessmentList.get(getAdapterPosition()).getCourseID()));
                 i.putExtra("startDate", String.valueOf(assessmentList.get(getAdapterPosition()).getStartDate()));
                 i.putExtra("endDate", String.valueOf(assessmentList.get(getAdapterPosition()).getEndDate()));
                 i.putExtra("title", String.valueOf(assessmentList.get(getAdapterPosition()).getTitle()));
                 i.putExtra("assessmentType", String.valueOf(assessmentList.get(getAdapterPosition()).getAssessmentType()));
                 i.putExtra("status", String.valueOf(assessmentList.get(getAdapterPosition()).getStatus()));
                 i.putExtra("startAlert", String.valueOf(assessmentList.get(getAdapterPosition()).getStartAlert()));
                 i.putExtra("endAlert", String.valueOf(assessmentList.get(getAdapterPosition()).getEndAlert()));
                 view.getContext().startActivity(i);
             }
         });
     }

 }
    public assessmentAdapter(ArrayList<Assessment> al){
        assessmentList = al;

    }
    @NonNull
    @Override
    public assessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assessment,parent,false);
        assessmentViewHolder avh = new assessmentViewHolder(view);
        return avh;
    }

    @Override
    public void onBindViewHolder(@NonNull assessmentViewHolder holder, int position) {
    Assessment currentAssessment = assessmentList.get(position);
    holder.assessmentTitleLabel.setText(currentAssessment.getTitle());
    holder.startDateLabel.setText(currentAssessment.getStartDate());
    holder.endDateLabel.setText(currentAssessment.getEndDate());




    }

    @Override
    public int getItemCount() {
        return assessmentList.size();
    }
}
