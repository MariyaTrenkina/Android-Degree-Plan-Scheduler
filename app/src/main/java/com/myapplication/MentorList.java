package com.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MentorList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Lists lists = new Lists();
    DBHelper db = new DBHelper(this);
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_mentor);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_mentors);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //Mentor list Start
        lists.clearMentor();
        Cursor mentorCursor = db.getAllMentors();
        if (mentorCursor.moveToFirst()) {
            do {
                int c_courseMentorID = mentorCursor.getInt(mentorCursor.getColumnIndex("courseMentorID"));
                String c_mentorName = mentorCursor.getString(mentorCursor.getColumnIndex("name"));
                String c_mentorPhoneNumber = mentorCursor.getString(mentorCursor.getColumnIndex("phoneNumber"));
                String c_mentorEmailAddress = mentorCursor.getString(mentorCursor.getColumnIndex("emailAddress"));
                if (!mentorCursor.getString(mentorCursor.getColumnIndex("name")).equals("NOT_ASSIGNED")) {
                    lists.addMentor(new Mentor(c_courseMentorID, c_mentorName, c_mentorPhoneNumber, c_mentorEmailAddress));
                }
            } while (mentorCursor.moveToNext());
        }
        //Assessment list End
        ArrayList mentorList = lists.getAllMentor();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new mentorAdapter(lists.getAllMentor());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.mentorsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(MentorList.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(MentorList.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(MentorList.this,CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(MentorList.this,AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:
                        break;
                }
                return false;
            }
        });
        // --Menu Nav End--
    }

    public void addMentorBtn(View view) {
        Intent myIntent = new Intent(this, MentorDetailAdd.class);
        startActivity(myIntent);
    }
}
