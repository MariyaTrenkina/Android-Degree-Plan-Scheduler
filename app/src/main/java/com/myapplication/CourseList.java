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

public class CourseList extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Lists lists = new Lists();
    DBHelper db = new DBHelper(this);

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_course);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_courses);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        lists.clearCourse();
        //Add Courses to ArrayList
        Cursor cursor = db.getAllCourses();
        if (cursor.moveToFirst()){
            do{
                int courseID = cursor.getInt(cursor.getColumnIndex("courseID"));
                String note = cursor.getString(cursor.getColumnIndex("note"));
                int termID = cursor.getInt(cursor.getColumnIndex("termID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                String status = cursor.getString(cursor.getColumnIndex("status"));
                int courseMentorID = cursor.getInt(cursor.getColumnIndex("courseMentorID"));
                int startAlert = cursor.getInt(cursor.getColumnIndex("startAlert"));
                int endAlert = cursor.getInt(cursor.getColumnIndex("endAlert"));
                if (!cursor.getString(cursor.getColumnIndex("title")).equals("NOT_ASSIGNED")) {
                    lists.addCourse(new Course(courseID, note, termID, title, startDate, endDate, status, courseMentorID,startAlert,endAlert));
                }
            }while(cursor.moveToNext());
        }
        ArrayList courseList = lists.getAllCourse();
        recyclerView = findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new courseAdapter(lists.getAllCourse());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.coursesBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(CourseList.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(CourseList.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:

                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(CourseList.this,AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(CourseList.this,MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                return false;

            }
        });
        // --Menu Nav End--
    }

    public void addCourseBtn(View view) {
        Intent myIntent = new Intent(this, CourseDetailAdd.class);
        startActivity(myIntent);
    }
}
