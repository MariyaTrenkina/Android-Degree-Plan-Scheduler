package com.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class mentorAdapter extends RecyclerView.Adapter<mentorAdapter.mentorViewHolder> {
    private ArrayList<Mentor> mentorList;
 public class mentorViewHolder extends RecyclerView.ViewHolder {
    public TextView mentorNameLabel;
    public TextView mentorEmailLabel;
    public TextView mentorPhoneLabel;

     public mentorViewHolder(@NonNull View itemView) {
         super(itemView);
         mentorNameLabel = itemView.findViewById(R.id.mentorNameLabel);
         mentorEmailLabel = itemView.findViewById(R.id.mentorEmailLabel);
         mentorPhoneLabel = itemView.findViewById(R.id.mentorPhoneLabel);
         itemView.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Intent i = new Intent(itemView.getContext(), MentorDetailModify.class);
                 i.putExtra("courseMentorID", String.valueOf(mentorList.get(getAdapterPosition()).getCourseMentorID()));
                 i.putExtra("name", String.valueOf(mentorList.get(getAdapterPosition()).getName()));
                 i.putExtra("emailAddress", String.valueOf(mentorList.get(getAdapterPosition()).getEmailAddress()));
                 i.putExtra("phoneNumber", String.valueOf(mentorList.get(getAdapterPosition()).getPhoneNumber()));

                 view.getContext().startActivity(i);
             }
         });
     }

 }
    public mentorAdapter(ArrayList<Mentor> ml){
        mentorList = ml;

    }
    @NonNull
    @Override
    public mentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mentor,parent,false);
        mentorViewHolder mvh = new mentorViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(@NonNull mentorViewHolder holder, int position) {
    Mentor currentMentor = mentorList.get(position);
    holder.mentorNameLabel.setText(currentMentor.getName());
    holder.mentorEmailLabel.setText(currentMentor.getEmailAddress());
    holder.mentorPhoneLabel.setText(currentMentor.getPhoneNumber());




    }

    @Override
    public int getItemCount() {
        return mentorList.size();
    }
}
