package com.myapplication;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AssessmentDetailAdd extends AppCompatActivity {
    TextView assessmentTitle;
    TextView assessmentStartDate;
    TextView assessmentEndDate;
    CheckBox assessmentStartCheckBox;
    CheckBox assessmentEndCheckBox;
    AppCompatRadioButton performanceRadioBtn;
    AppCompatRadioButton objectiveRadioBtn;
    Button startDate;
    Button endDate;
    Button deleteBtn;
    DBHelper db = new DBHelper(this);
    Lists lists = new Lists();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_assessment);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_assessments);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.assessmentsBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(AssessmentDetailAdd.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(AssessmentDetailAdd.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(AssessmentDetailAdd.this,CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:

                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(AssessmentDetailAdd.this,MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                return false;

            }
        });
        // --Menu Nav End--

        //Spinner
        Spinner spinner = findViewById(R.id.statusSpinner);
        String[] statusItems = new String[]{"Not Started", "In Progress", "Completed"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusItems);
        spinner.setAdapter(spinnerAdapter);
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
    public void startDateBtn(View view) {
        startDate = findViewById(R.id.assessmentStartDate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String monthStr = String.format("%02d", month+1);
                String dayStr = String.format("%02d", day);
                startDate.setText(monthStr + "/" + dayStr + "/" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    public void endDateBtn(View view) {
        endDate = findViewById(R.id.assessmentEndDate);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String monthStr = String.format("%02d", month+1);
                String dayStr = String.format("%02d", day);
                endDate.setText(monthStr + "/" + dayStr + "/" + year);
            }
        }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
    public boolean validateInputs(String finalAssessmentTitle, String finalAssessmentStartDate, String finalAssessmentEndDate){
        // Validate Assessment Title
        if (finalAssessmentTitle.isEmpty()){
            alertMessage("Assessment title is empty!");
            return false;
        }
        // Validate Assessment Start Date
        if (!finalAssessmentStartDate.matches("^(\\d{1,2})(\\/|-)(\\d{1,2})(\\/|-)(\\d{4})$")){
            alertMessage("Assessment start date not set!");
            return false;
        }
        // Validate Assessment End Date
        if (!finalAssessmentEndDate.matches("^(\\d{1,2})(\\/|-)(\\d{1,2})(\\/|-)(\\d{4})$")){
            alertMessage("Assessment end date not set!");
            return false;
        }
        if (!performanceRadioBtn.isChecked() && !objectiveRadioBtn.isChecked()){
            alertMessage("Assessment type not set!");
            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveBtn(View view) {

        assessmentTitle = findViewById(R.id.assessmentTitleTxtBox);
        assessmentStartDate = findViewById(R.id.assessmentStartDate);
        assessmentEndDate = findViewById(R.id.assessmentEndDate);
        assessmentStartCheckBox = findViewById(R.id.assessmentStartCheckBox);
        assessmentEndCheckBox = findViewById(R.id.assessmentEndCheckBox);
        performanceRadioBtn = findViewById(R.id.performanceRadioBtn);
        objectiveRadioBtn = findViewById(R.id.objectiveRadioBtn);
        Spinner spinner = findViewById(R.id.statusSpinner);

        Intent i = getIntent();
        String finalAssessmentType = null;
        String finalAssessmentTitle = assessmentTitle.getText().toString();
        String finalAssessmentStartDate = assessmentStartDate.getText().toString();
        String finalAssessmentEndDate = assessmentEndDate.getText().toString();
        String finalStatus = spinner.getSelectedItem().toString();
        if (performanceRadioBtn.isChecked()){
            finalAssessmentType = "P";
        }
        if (objectiveRadioBtn.isChecked()){
            finalAssessmentType = "O";
        }
        Boolean finalAssessmentStartCheckBox = assessmentStartCheckBox.isChecked();
        Boolean finalAssessmentEndCheckBox = assessmentEndCheckBox.isChecked();
        int startCheckBoxInt;
        int endCheckBoxInt;

        //Update Term in SQL
        if (validateInputs(finalAssessmentTitle, finalAssessmentStartDate, finalAssessmentEndDate)) {
            if (finalAssessmentStartCheckBox == true) {
                startCheckBoxInt = 1;
            } else {
                startCheckBoxInt = 0;
            }
            if (finalAssessmentEndCheckBox == true) {
                endCheckBoxInt = 1;
            } else {
                endCheckBoxInt = 0;
            }
            db.insertAssessment(1, finalAssessmentTitle, finalAssessmentType, finalStatus, finalAssessmentStartDate,finalAssessmentEndDate, startCheckBoxInt, endCheckBoxInt);

            //Update Assessment Array list from SQL--Start
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
            //Update Assessments Array list from SQL--End
            //Set Course Alerts
            setCourseAlerts();
            //Set Assessment Alerts
            setAssessmentAlerts();
            //Go to Assessment List Activity

            Intent myIntent = new Intent(this, AssessmentList.class);
            startActivity(myIntent);
        }
    }
    public void cancelBtn(View view) {
        Intent myIntent = new Intent(this, AssessmentList.class);
        startActivity(myIntent);


    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setCourseAlerts(){
        //Alert - START


        /////////////////////////////

        Lists lists = new Lists();
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
        ArrayList<Course> courseList = lists.getAllCourse();
        //Remove all alerts
        for(int i=100;i>100+(courseList.size()*2);i++){
            Intent NotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(this.getApplicationContext(), i, NotificationIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pIntent);
        }
        int counter = 100;
        for(Course c : courseList){
            //


            if(c.getStartAlert() == 1){
                String startNotificationTitle = "Course Start Reminder";
                String startNotificationText = c.getTitle() + " began on "+c.getStartDate();

                Intent startNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                startNotificationIntent.putExtra("notifyTitle", startNotificationTitle);
                startNotificationIntent.putExtra("notifyContent", startNotificationText);
                counter++;
                startNotificationIntent.putExtra("counter",counter);

                PendingIntent startPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), counter, startNotificationIntent, 0);
                Log.d("Alarm: ", "Counter: "+counter);
                AlarmManager startAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    //start alarm
                    Date startDate = dateFormat.parse(c.getStartDate());
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);

                    startAlarmManager.set(AlarmManager.RTC_WAKEUP, startCal.getTimeInMillis(), startPendingIntent);


                    Log.d("Alarm: ", "Set alarm for "+c.getTitle());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(c.getEndAlert() == 1){
                //end course alarm setup
                String endNotificationTitle = "Course End Reminder";
                String endNotificationText = c.getTitle() + " ended on "+c.getEndDate();

                Intent endNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                endNotificationIntent.putExtra("notifyTitle", endNotificationTitle);
                endNotificationIntent.putExtra("notifyContent", endNotificationText);
                counter++;
                endNotificationIntent.putExtra("counter",counter);
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), counter, endNotificationIntent, 0);
                AlarmManager endAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    //end alarm
                    Date endDate = dateFormat.parse(c.getEndDate());
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(endDate);
                    endAlarmManager.set(AlarmManager.RTC_WAKEUP, endCal.getTimeInMillis(), endPendingIntent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }
//Course Alert - END
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setAssessmentAlerts(){
        //Assessment Alert - START
        Lists lists = new Lists();
        //Assessment list Start
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
        //Assessment list End
        ArrayList<Assessment> assessmentList = lists.getAllAssessment();
        //Remove all alerts
        for(int i=1000;i>1000+(assessmentList.size()*2);i++){
            Intent NotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pIntent = PendingIntent.getBroadcast(this.getApplicationContext(), i, NotificationIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pIntent);
        }
        int counter = 1000;
        for(Assessment a : assessmentList){
            //


            if(a.getStartAlert() == 1){
                //startAssessment alarm setup
                String startNotificationTitle = "Assessment Start Reminder";
                String startNotificationText = a.getTitle() + " began on "+a.getStartDate();

                Intent startNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                startNotificationIntent.putExtra("notifyTitle", startNotificationTitle);
                startNotificationIntent.putExtra("notifyContent", startNotificationText);
                counter++;
                startNotificationIntent.putExtra("counter",counter);

                PendingIntent startPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), counter, startNotificationIntent, 0);
                Log.d("Alarm: ", "Counter: "+counter);
                AlarmManager startAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    //start alarm
                    Date startDate = dateFormat.parse(a.getStartDate());
                    Calendar startCal = Calendar.getInstance();
                    startCal.setTime(startDate);

                    startAlarmManager.set(AlarmManager.RTC_WAKEUP, startCal.getTimeInMillis(), startPendingIntent);


                    Log.d("Alarm: ", "Set alarm for "+a.getTitle());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if(a.getEndAlert() == 1){
                //endAssessment alarm setup
                String endNotificationTitle = "Assessment End Reminder";
                String endNotificationText = a.getTitle() + " ended on "+a.getEndDate();

                Intent endNotificationIntent = new Intent(this.getApplicationContext(), AlarmReceiver.class);
                endNotificationIntent.putExtra("notifyTitle", endNotificationTitle);
                endNotificationIntent.putExtra("notifyContent", endNotificationText);
                counter++;
                endNotificationIntent.putExtra("counter",counter);
                PendingIntent endPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), counter, endNotificationIntent, 0);
                AlarmManager endAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    //end alarm
                    Date endDate = dateFormat.parse(a.getEndDate());
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(endDate);
                    endAlarmManager.set(AlarmManager.RTC_WAKEUP, endCal.getTimeInMillis(), endPendingIntent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }
//Assessment Alert - END
    }


}
