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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MentorDetailModify extends AppCompatActivity {
    TextView mentorNameEditText;
    TextView mentorEmailEditText;
    TextView mentorPhoneEditText;
    Button deleteBtn;
    DBHelper db = new DBHelper(this);
    Lists lists = new Lists();


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_mentor);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_mentors);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        mentorNameEditText = findViewById(R.id.mentorNameEditText);
        mentorEmailEditText = findViewById(R.id.mentorEmailEditText);
        mentorPhoneEditText = findViewById(R.id.mentorPhoneEditText);
        //Import Info from Mentor - Start
        Intent i = getIntent();
        int courseMentorID = Integer.parseInt(i.getStringExtra("courseMentorID"));
        String name = i.getStringExtra("name");
        String emailAddress = i.getStringExtra("emailAddress");
        String phoneNumber = i.getStringExtra("phoneNumber");
        //Import Info from Mentor - End
        //Set text from Mentor Info - Start
        mentorNameEditText.setText(name);
        mentorEmailEditText.setText(emailAddress);
        mentorPhoneEditText.setText(phoneNumber);
        //Set text from Mentor Info - End

        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.mentorsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(MentorDetailModify.this, MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(MentorDetailModify.this, TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(MentorDetailModify.this, CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(MentorDetailModify.this, AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:

                        break;
                }
                return false;

            }
        });

        // --Menu Nav End--
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
        //Mentor list End
        //Delete Button
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setEnabled(true);
    }


    public void alertMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create();
        builder.show();
    }

    public boolean validateInputs(String finalMentorNameEditText, String finalMentorEmailEditText, String finalMentorPhoneEditText){
        // Validate Mentor Name
        if (finalMentorNameEditText.isEmpty()){
            alertMessage("Mentor Name is empty!");
            return false;
        }
        // Validate Mentor Email Address
        if (finalMentorEmailEditText.isEmpty()){
            alertMessage("Mentor email address is empty!");
            return false;
        }
        // Validate Mentor Email Address
        if (!finalMentorEmailEditText.matches("^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")){
            alertMessage("Mentor email address not valid!");
            return false;
        }

        // Validate Mentor Phone Number
        if (finalMentorPhoneEditText.isEmpty()){
            alertMessage("Mentor phone number is empty!");
            return false;
        }

        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveBtn(View view) {

        mentorNameEditText = findViewById(R.id.mentorNameEditText);
        mentorEmailEditText = findViewById(R.id.mentorEmailEditText);
        mentorPhoneEditText = findViewById(R.id.mentorPhoneEditText);
        //Import Info from Mentor - Start
        Intent i = getIntent();
        int courseMentorID = Integer.parseInt(i.getStringExtra("courseMentorID"));
        String name = i.getStringExtra("name");
        String emailAddress = i.getStringExtra("emailAddress");
        String phoneNumber = i.getStringExtra("phoneNumber");
        //Import Info from Mentor - End
        String finalMentorNameEditText = mentorNameEditText.getText().toString();
        String finalMentorEmailEditText = mentorEmailEditText.getText().toString();
        String finalMentorPhoneEditText = mentorPhoneEditText.getText().toString();

        if (validateInputs(finalMentorNameEditText, finalMentorEmailEditText, finalMentorPhoneEditText)) {

            db.updateMentor(finalMentorNameEditText,finalMentorPhoneEditText,finalMentorEmailEditText,courseMentorID);

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
            //Mentor list End


            //Go to Mentor List Activity

            Intent myIntent = new Intent(this, MentorList.class);
            startActivity(myIntent);
        }
    }

    public void cancelBtn(View view) {
        Intent myIntent = new Intent(this, MentorList.class);
        startActivity(myIntent);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void deleteBtn(View view) {
        String finalMentorNameEditText = mentorNameEditText.getText().toString();
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
                        int courseMentorID = Integer.parseInt(i.getStringExtra("courseMentorID"));

                        db.deleteMentor(courseMentorID);
                        Iterator<Course> iterator = lists.getAllCourse().iterator();
                        while (iterator.hasNext()) {
                            Course course = iterator.next();
                            if (course.getCourseMentorID() == courseMentorID) {
                                Log.d("Testing", "CURRENT courseMentorID: "+courseMentorID);
                                Log.d("Testing", "course.getCourseMentorID(): "+course.getCourseMentorID());
                                db.updateCourseMentorIDInCourses(1,course.getCourseID());
                            }
                        }

                        Intent myIntent = new Intent(getBaseContext(), MentorList.class);
                        startActivity(myIntent);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //Do Nothing and return back to MentorDetailModify Activity
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete: " + finalMentorNameEditText+ "?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
        ///



    }

}
