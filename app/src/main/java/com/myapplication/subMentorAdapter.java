package com.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class subMentorAdapter extends RecyclerView.Adapter<subMentorAdapter.mentorViewHolder> {
    static int mentorChecked = 100;
    static private ArrayList<Mentor> mentorList;

    static public int courseMentorID;
 public static class mentorViewHolder extends RecyclerView.ViewHolder {

     public TextView name;
     public TextView email;
     public TextView phone;
     public SwitchCompat mentorSublistToggle;
     public ImageView checkmark;

     public mentorViewHolder(@NonNull View itemView) {
         super(itemView);

         name = itemView.findViewById(R.id.mentorNameLabel);
         email = itemView.findViewById(R.id.mentorEmailLabel);
         phone = itemView.findViewById(R.id.mentorPhoneLabel);
         mentorSublistToggle = itemView.findViewById(R.id.mentorSublistToggle);
         checkmark = itemView.findViewById(R.id.checkmark);

         itemView.setOnClickListener(new View.OnClickListener() {


             @Override
             public void onClick(View view) {
                 Log.d("Test TAG", "adapter pos: " + getAdapterPosition());
                if (mentorChecked == 100 || mentorChecked == getAdapterPosition()) {
                    ImageView checkMark = itemView.findViewById(R.id.checkmark);
                    SwitchCompat mentorSublistToggle = itemView.findViewById(R.id.mentorSublistToggle);
                    if (checkMark.getVisibility() == View.INVISIBLE) {
                        mentorChecked = getAdapterPosition();
                        checkMark.setVisibility(View.VISIBLE);
                        mentorSublistToggle.setChecked(true);

                    } else {
                        mentorChecked = 100;
                        checkMark.setVisibility(View.INVISIBLE);
                        mentorSublistToggle.setChecked(false);

                    }
                }else{

                    Log.d("Test TAG", "MENTOR IS CHECKED ALREADY");
                }

             }

         });

     }

 }
    public subMentorAdapter(ArrayList<Mentor> ml,int courseMentor_ID){
     mentorList = ml;

     courseMentorID = courseMentor_ID;

    }

    @NonNull
    @Override
    public mentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subitem_mentor,parent,false);
        mentorViewHolder cvh = new mentorViewHolder(view);
        return cvh;
    }



    @Override
    public void onBindViewHolder(@NonNull mentorViewHolder holder, int position) {

    Mentor currentMentor = mentorList.get(position);
    holder.name.setText(currentMentor.getName());
    holder.email.setText(currentMentor.getEmailAddress());
    holder.phone.setText(currentMentor.getPhoneNumber());

        if (currentMentor.getCourseMentorID()==courseMentorID) {

            holder.checkmark.setVisibility(View.VISIBLE);
            holder.mentorSublistToggle.setChecked(true);
            mentorChecked = position;

        } else {
            holder.checkmark.setVisibility(View.INVISIBLE);
            holder.mentorSublistToggle.setChecked(false);

        }

    }

    @Override
    public int getItemCount() {
        return mentorList.size();

    }



}

