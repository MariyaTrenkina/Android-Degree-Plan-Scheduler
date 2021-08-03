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

public class AssessmentList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Lists lists = new Lists();
    DBHelper db = new DBHelper(this);
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_assessment);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_assessments);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //Assessment Sublist Start
        lists.clearAssessment();
        Cursor assessmentCursor = db.getAllAssessments();
        if (assessmentCursor.moveToFirst()) {
            do {
                int c_assessmentID = assessmentCursor.getInt(assessmentCursor.getColumnIndex("assessmentID"));
                int c_courseID = assessmentCursor.getInt(assessmentCursor.getColumnIndex("courseID"));
                String c_title = assessmentCursor.getString(assessmentCursor.getColumnIndex("title"));
                String c_assessmentType = assessmentCursor.getString(assessmentCursor.getColumnIndex("assessmentType"));
                String c_status = assessmentCursor.getString(assessmentCursor.getColumnIndex("status"));
                String c_startDate = assessmentCursor.getString(assessmentCursor.getColumnIndex("startDate"));
                String c_endDate = assessmentCursor.getString(assessmentCursor.getColumnIndex("endDate"));
                int c_startAlert = assessmentCursor.getInt(assessmentCursor.getColumnIndex("startAlert"));
                int c_endAlert = assessmentCursor.getInt(assessmentCursor.getColumnIndex("endAlert"));
                if (!assessmentCursor.getString(assessmentCursor.getColumnIndex("title")).equals("NOT_ASSIGNED")) {
                    lists.addAssessment(new Assessment(c_assessmentID, c_courseID, c_title, c_assessmentType,c_status,c_startDate, c_endDate, c_startAlert, c_endAlert));
                }
            } while (assessmentCursor.moveToNext());
        }
        //Assessment Sublist End
        ArrayList assessmentList = lists.getAllAssessment();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new assessmentAdapter(lists.getAllAssessment());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.assessmentsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(AssessmentList.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(AssessmentList.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(AssessmentList.this,CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:

                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(AssessmentList.this,MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                return false;

            }
        });
        // --Menu Nav End--
    }
    public void addAssessmentBtn(View view) {
        Intent myIntent = new Intent(this, AssessmentDetailAdd.class);
        startActivity(myIntent);
    }
}
