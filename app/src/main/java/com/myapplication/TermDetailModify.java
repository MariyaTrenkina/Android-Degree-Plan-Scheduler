package com.myapplication;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class TermDetailModify extends AppCompatActivity {
    TextView termName;
    TextView termStartDate;
    TextView termEndDate;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Button startDate;
    Button endDate;
    DBHelper db = new DBHelper(this);
    Lists lists = new Lists();
    Button deleteBtn;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_term);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_terms);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        subCourseAdapter.coursesChecked.clear();
        subCourseAdapter.coursesPreviousChecked.clear();
        termName = findViewById(R.id.termName);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String startDate = i.getStringExtra("startDate");
        String endDate = i.getStringExtra("endDate");
        int termID = Integer.parseInt(i.getStringExtra("termID"));
        termName.setText(name);
        termStartDate.setText(startDate);
        termEndDate.setText(endDate);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.termsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(TermDetailModify.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(TermDetailModify.this, CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(TermDetailModify.this, AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(TermDetailModify.this, MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                return false;

            }
        });

        // --Menu Nav End--
        //Course Sublist Start
        lists.clearCourse();
        Cursor cursor = db.getAllCourses();
        if (cursor.moveToFirst()) {
            do {
                int c_courseID = cursor.getInt(cursor.getColumnIndex("courseID"));
                String c_note = cursor.getString(cursor.getColumnIndex("note"));
                int c_termID = cursor.getInt(cursor.getColumnIndex("termID"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String c_startDate = cursor.getString(cursor.getColumnIndex("startDate"));
                String c_endDate = cursor.getString(cursor.getColumnIndex("endDate"));
                String c_status = cursor.getString(cursor.getColumnIndex("status"));
                int c_courseMentorID = cursor.getInt(cursor.getColumnIndex("courseMentorID"));
                int c_startAlert = cursor.getInt(cursor.getColumnIndex("startAlert"));
                int c_endAlert = cursor.getInt(cursor.getColumnIndex("endAlert"));
                if (!cursor.getString(cursor.getColumnIndex("title")).equals("NOT_ASSIGNED")) {
                    lists.addCourse(new Course(c_courseID, c_note, c_termID, title, c_startDate, c_endDate, c_status, c_courseMentorID, c_startAlert, c_endAlert));
                }
            } while (cursor.moveToNext());
        }
        recyclerView = findViewById(R.id.courseSublistRecyclerView);
        layoutManager = new LinearLayoutManager(this);
        adapter = new subCourseAdapter(lists.getAllCourse(),termID);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setEnabled(true);
    }


    public void startDateBtn(View view) {
        startDate = findViewById(R.id.termStartDate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                startDate.setText((month+1) + "/" + day + "/" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    public void endDateBtn(View view) {
        endDate = findViewById(R.id.termEndDate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                endDate.setText((month+1) + "/" + day + "/" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    public void alertMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create();
        builder.show();
    }
    public boolean validateInputs(String finalTermName, String finalTermStartDate, String finalTermEndDate){
        // Validate Course Title
        if (finalTermName.isEmpty()){
            alertMessage("Course title is empty!");
            return false;
        }
        // Validate Course Start Date
        if (!finalTermStartDate.matches("^(\\d{1,2})(\\/|-)(\\d{1,2})(\\/|-)(\\d{4})$")){
            alertMessage("Course start date not set!");
            return false;
        }
        // Validate Course End Date
        if (!finalTermEndDate.matches("^(\\d{1,2})(\\/|-)(\\d{1,2})(\\/|-)(\\d{4})$")){
            alertMessage("Course end date not set!");
            return false;
        }

        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveBtn(View view) {
        Intent i = getIntent();
        int termID = Integer.parseInt(i.getStringExtra("termID"));
        termName = findViewById(R.id.termName);
        termStartDate = findViewById(R.id.termStartDate);
        termEndDate = findViewById(R.id.termEndDate);

        String finalTermName = termName.getText().toString();
        String finalTermStartDate =termStartDate.getText().toString();
        String finalTermEndDate = termEndDate.getText().toString();

//        //Check Course Check Mark Status - Start
        if (validateInputs(finalTermName, finalTermStartDate, finalTermEndDate)) {

            //Check Assessment Check Mark Status - Start
            subCourseAdapter.coursesChecked.forEach((n) -> db.updateTermInCourses(n, termID));
            subCourseAdapter.coursesPreviousChecked.removeAll(subAssessmentAdapter.assessmentsChecked);
            ArrayList<Integer> removedCourses = subCourseAdapter.coursesPreviousChecked;
            removedCourses.forEach((n) -> db.updateTermInCourses(n, 1));
//            //Check Course Check Mark Status - End

            //Update Term in ArrayList
            Iterator<Term> iterator = lists.getAllTerms().iterator();
            while (iterator.hasNext()) {
                Term term = iterator.next();
                if (term.getTermID() == termID) {
                    term.setName(finalTermName);
                    term.setStartDate(finalTermStartDate);
                    term.setEndDate(finalTermEndDate);
                }
            }
            //Update Term in SQL


            db.updateTerm(finalTermName, finalTermStartDate, finalTermEndDate, termID);
            //Go to Term List Activity
            Intent myIntent = new Intent(this, TermList.class);
            startActivity(myIntent);
        }
    }

    public void cancelBtn(View view) {
        Intent myIntent = new Intent(this, TermList.class);
        startActivity(myIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteBtn(View view) {
        String finalTermName = termName.getText().toString();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Add Courses to ArrayList
                        lists.clearCourse();
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
                                    Log.d("Testing ", "ADDED Course: "+courseID);
                                    lists.addCourse(new Course(courseID, note, termID, title, startDate, endDate, status, courseMentorID,startAlert,endAlert));
                                }
                            }while(cursor.moveToNext());
                        }
                        ArrayList courseList = lists.getAllCourse();
                        Intent i = getIntent();
                        int termID = Integer.parseInt(i.getStringExtra("termID"));


                        int courseIsAssigned = 0;
                        Iterator<Course> iterator = lists.getAllCourse().iterator();
                        while (iterator.hasNext()) {
                            Course course = iterator.next();
                            if (course.getTermID() == termID && termID != 1) {
                                courseIsAssigned = 1;
                            }
                        }
                        if (courseIsAssigned==0) {
                            db.deleteTerm(termID);
                            Intent myIntent = new Intent(getBaseContext(), TermList.class);
                            startActivity(myIntent);
                        }else{
                            alertMessage("Can not delete! Term has courses assigned to it!");
                        }
                        break;


                    case DialogInterface.BUTTON_NEGATIVE:
                        //Do Nothing and return back to MentorDetailModify Activity
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete: " + finalTermName+ "?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        ///



    }
}
