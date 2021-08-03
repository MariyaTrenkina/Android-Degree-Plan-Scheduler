package com.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class termAdapter extends RecyclerView.Adapter<termAdapter.termViewHolder> {
    private ArrayList<Term> termList;
 public class termViewHolder extends RecyclerView.ViewHolder {
    public TextView textView1;
    public TextView textView2;
    public TextView textView3;
     public termViewHolder(@NonNull View itemView) {
         super(itemView);
         textView1 = itemView.findViewById(R.id.termNameLabel);
         textView2 = itemView.findViewById(R.id.startDateLabel);
         textView3 = itemView.findViewById(R.id.endDateLabel);
         itemView.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 Intent i = new Intent(itemView.getContext(), TermDetailModify.class);
                 i.putExtra("name", String.valueOf(termList.get(getAdapterPosition()).getName()));
                 i.putExtra("startDate", String.valueOf(termList.get(getAdapterPosition()).getStartDate()));
                 i.putExtra("endDate", String.valueOf(termList.get(getAdapterPosition()).getEndDate()));
                 i.putExtra("termID", String.valueOf(termList.get(getAdapterPosition()).getTermID()));
                 view.getContext().startActivity(i);
             }
         });
     }

 }
    public  termAdapter(ArrayList<Term> tl){
     termList = tl;

    }
    @NonNull
    @Override
    public termViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_term,parent,false);
        termViewHolder tvh = new termViewHolder(view);
        return tvh;
    }

    @Override
    public void onBindViewHolder(@NonNull termViewHolder holder, int position) {
    Term currentTerm = termList.get(position);
    holder.textView1.setText(currentTerm.getName());
    holder.textView2.setText(currentTerm.getStartDate());
    holder.textView3.setText(currentTerm.getEndDate());
    }

    @Override
    public int getItemCount() {
        return termList.size();
    }
}
