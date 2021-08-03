package com.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class TermList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Lists lists = new Lists();
    DBHelper db = new DBHelper(this);
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_term);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_terms);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        lists.clearTerm();
        //Add Terms to ArrayList
        Cursor cursor = db.getAllTerms();
        if (cursor.moveToFirst()){
            do{
                int termID = cursor.getInt(cursor.getColumnIndex("termID"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                if (!cursor.getString(cursor.getColumnIndex("name")).equals("NOT_ASSIGNED")) {
                    lists.addTerm(new Term(termID, name, startDate, endDate));
                }
            }while(cursor.moveToNext());
        }
        ArrayList termList = lists.getAllTerms();
     recyclerView = findViewById(R.id.recyclerView);
     layoutManager = new LinearLayoutManager(this);
     adapter = new termAdapter(lists.getAllTerms());

    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setAdapter(adapter);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.termsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(TermList.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(TermList.this,CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(TermList.this,AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(TermList.this,MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                return false;

            }
        });
        // --Menu Nav End--
    }

    public void addTermBtn(View view) {
        Intent myIntent = new Intent(this, TermDetailAdd.class);
        startActivity(myIntent);
    }

}
