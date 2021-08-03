package com.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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

import java.util.Calendar;

public class MentorDetailAdd extends AppCompatActivity {
    TextView mentorNameEditText;
    TextView mentorEmailEditText;
    TextView mentorPhoneEditText;
    Button deleteBtn;
    DBHelper db = new DBHelper(this);
    Lists lists = new Lists();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_mentor);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_mentors);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.mentorsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(MentorDetailAdd.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(MentorDetailAdd.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(MentorDetailAdd.this,CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(MentorDetailAdd.this,AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:

                        break;
                }
                return false;

            }
        });
        // --Menu Nav End--

        //Delete Button
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setEnabled(false);
        deleteBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

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
        Intent i = getIntent();
        String finalMentorNameEditText = mentorNameEditText.getText().toString();
        String finalMentorEmailEditText = mentorEmailEditText.getText().toString();
        String finalMentorPhoneEditText = mentorPhoneEditText.getText().toString();


        //Update Term in SQL
        if (validateInputs(finalMentorNameEditText, finalMentorEmailEditText, finalMentorPhoneEditText)) {

            db.insertMentor(finalMentorNameEditText,finalMentorPhoneEditText,finalMentorEmailEditText);

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



}
