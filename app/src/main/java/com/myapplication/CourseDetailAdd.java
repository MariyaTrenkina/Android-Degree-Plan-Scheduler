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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CourseDetailAdd extends AppCompatActivity {
    TextView courseTitle;
    TextView courseStartDate;
    TextView courseEndDate;
    CheckBox courseStartCheckBox;
    CheckBox courseEndCheckBox;
    TextView notesTxtBox;
    EditText phoneSMSTxtBox;
    private RecyclerView assessmentRecyclerView;
    private RecyclerView.Adapter assessmentAdapter;
    private RecyclerView mentorRecyclerView;
    private RecyclerView.Adapter mentorAdapter;
    private RecyclerView.LayoutManager assessmentLayoutManager;
    private RecyclerView.LayoutManager mentorLayoutManager;
    Button startDate;
    Button endDate;
    Button deleteBtn;
    Button shareBtn;
    DBHelper db = new DBHelper(this);
    Lists lists = new Lists();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        int courseID = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_course);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_courses);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        subAssessmentAdapter.assessmentsChecked.clear();
        subAssessmentAdapter.assessmentsPreviousChecked.clear();
        //--Menu Nav Start--
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.coursesBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        Intent home = new Intent(CourseDetailAdd.this,MainActivity.class);
                        startActivity(home);
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(CourseDetailAdd.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:

                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(CourseDetailAdd.this,AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(CourseDetailAdd.this,MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                return false;

            }
        });
        // --Menu Nav End--
        //Assessment Sublist Start
        lists.clearAssessment();
        Cursor assessmentCursor = db.getAllAssessments();
        if (assessmentCursor.moveToFirst()) {
            do {
                int c_assessmentID = assessmentCursor.getInt(assessmentCursor.getColumnIndex("assessmentID"));
                int c_courseID = assessmentCursor.getInt(assessmentCursor.getColumnIndex("courseID"));
                String title = assessmentCursor.getString(assessmentCursor.getColumnIndex("title"));
                String c_assessmentType = assessmentCursor.getString(assessmentCursor.getColumnIndex("assessmentType"));
                String c_status = assessmentCursor.getString(assessmentCursor.getColumnIndex("status"));
                String c_startDate = assessmentCursor.getString(assessmentCursor.getColumnIndex("startDate"));
                String c_endDate = assessmentCursor.getString(assessmentCursor.getColumnIndex("endDate"));
                int c_startAlert = assessmentCursor.getInt(assessmentCursor.getColumnIndex("startAlert"));
                int c_endAlert = assessmentCursor.getInt(assessmentCursor.getColumnIndex("endAlert"));
                if (!assessmentCursor.getString(assessmentCursor.getColumnIndex("title")).equals("NOT_ASSIGNED")) {
                    lists.addAssessment(new Assessment(c_assessmentID, c_courseID, title, c_assessmentType,c_status,c_startDate, c_endDate, c_startAlert, c_endAlert));
                }
            } while (assessmentCursor.moveToNext());
        }
        //Assessment Sublist End
        //Mentor Sublist Start
        lists.clearMentor();

        Cursor mentorCursor = db.getAllMentors();
        if (mentorCursor.moveToFirst()) {
            do {
                int c_courseMentorID = mentorCursor.getInt(mentorCursor.getColumnIndex("courseMentorID"));
                String mentorName = mentorCursor.getString(mentorCursor.getColumnIndex("name"));
                String c_mentorPhoneNumber = mentorCursor.getString(mentorCursor.getColumnIndex("phoneNumber"));
                String c_mentorEmailAddress = mentorCursor.getString(mentorCursor.getColumnIndex("emailAddress"));
                if (!mentorCursor.getString(mentorCursor.getColumnIndex("name")).equals("NOT_ASSIGNED")) {
                    lists.addMentor(new Mentor(c_courseMentorID, mentorName, c_mentorPhoneNumber, c_mentorEmailAddress));
                }
            } while (mentorCursor.moveToNext());
        }
        //Mentor Sublist End

        assessmentLayoutManager = new LinearLayoutManager(this);
        mentorLayoutManager = new LinearLayoutManager(this);
        //Assessment RecycleView
        assessmentRecyclerView = findViewById(R.id.assessmentsRecyclerView);
        assessmentAdapter = new subAssessmentAdapter(lists.getAllAssessment(),courseID);
        assessmentRecyclerView.setLayoutManager(assessmentLayoutManager);
        assessmentRecyclerView.setAdapter(assessmentAdapter);
        assessmentRecyclerView.setHasFixedSize(true);
        //Mentor RecycleView
        mentorRecyclerView = findViewById(R.id.mentorsRecyclerView);
        mentorAdapter = new subMentorAdapter(lists.getAllMentor(),courseID);
        mentorAdapter.setHasStableIds(true);
        mentorRecyclerView.setLayoutManager(mentorLayoutManager);
        mentorRecyclerView.setAdapter(mentorAdapter);
        mentorRecyclerView.setHasFixedSize(true);

        //Spinner
        Spinner spinner = findViewById(R.id.statusSpinner);
        String[] statusItems = new String[]{"Not Started", "In Progress", "Completed"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusItems);
        spinner.setAdapter(spinnerAdapter);
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setEnabled(false);
        deleteBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        //Share Button
        shareBtn = findViewById(R.id.shareBtn);
        shareBtn.setEnabled(false);
        shareBtn.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        //phoneSMSTxtBox
        phoneSMSTxtBox = findViewById(R.id.phoneSMSTxtBox);
        phoneSMSTxtBox.setEnabled(false);
        phoneSMSTxtBox.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));

    }
    public void alertMessage(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.create();
        builder.show();
    }
    public void startDateBtn(View view) {
        startDate = findViewById(R.id.courseStartDate);
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
        endDate = findViewById(R.id.courseEndDate);
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
    public boolean validateInputs(String finalCourseTitle, String finalCourseStartDate, String finalCourseEndDate){
        // Validate Course Title
        if (finalCourseTitle.isEmpty()){
            alertMessage("Course title is empty!");
            return false;
        }
        // Validate Course Start Date
        if (!finalCourseStartDate.matches("^(\\d{1,2})(\\/|-)(\\d{1,2})(\\/|-)(\\d{4})$")){
            alertMessage("Course start date not set!");
            return false;
        }
        // Validate Course End Date
        if (!finalCourseEndDate.matches("^(\\d{1,2})(\\/|-)(\\d{1,2})(\\/|-)(\\d{4})$")){
            alertMessage("Course end date not set!");
            return false;
        }

        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveBtn(View view) {

        int currentCourseID;
        int mentorChecked = subMentorAdapter.mentorChecked;
        courseTitle = findViewById(R.id.courseTitleTxtBox);
        courseStartDate = findViewById(R.id.courseStartDate);
        courseEndDate = findViewById(R.id.courseEndDate);
        courseStartCheckBox = findViewById(R.id.courseStartCheckBox);
        courseEndCheckBox = findViewById(R.id.courseEndCheckBox);
        notesTxtBox = findViewById(R.id.notesTxtBox);
        Spinner spinner = findViewById(R.id.statusSpinner);

        Intent i = getIntent();
        Log.d("Test TAG", "courseStartDate: " + courseStartDate.getText());
        Log.d("Test TAG", "courseStartDate.getText().toString();: " + courseStartDate.getText().toString());
        String finalCourseTitle = courseTitle.getText().toString();
        String finalCourseStartDate = courseStartDate.getText().toString();
        String finalCourseEndDate = courseEndDate.getText().toString();
        String finalStatus = spinner.getSelectedItem().toString();
        String finalNotes = notesTxtBox.getText().toString();
        Boolean finalCourseStartCheckBox = courseStartCheckBox.isChecked();
        Boolean finalCourseEndCheckBox = courseEndCheckBox.isChecked();
        int startCheckBoxInt;
        int endCheckBoxInt;

        //Update Term in SQL
        if (validateInputs(finalCourseTitle, finalCourseStartDate, finalCourseEndDate)) {
            if (finalCourseStartCheckBox == true) {
                startCheckBoxInt = 1;
            } else {
                startCheckBoxInt = 0;
            }
            if (finalCourseEndCheckBox == true) {
                endCheckBoxInt = 1;
            } else {
                endCheckBoxInt = 0;
            }
            db.insertCourses("NOTES", 1, finalCourseTitle, finalCourseStartDate, finalCourseEndDate, finalStatus, mentorChecked+2, startCheckBoxInt, endCheckBoxInt);

            //Update Courses Array list from SQL--Start
            lists.clearCourse();
            //Add Courses to ArrayList
            Cursor cursor = db.getAllCourses();
            if (cursor.moveToFirst()) {
                do {
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
                        lists.addCourse(new Course(courseID, note, termID, title, startDate, endDate, status, courseMentorID, startAlert, endAlert));
                    }
                } while (cursor.moveToNext());
            }
            //Update Courses Array list from SQL--End
            currentCourseID = getCurrentCourseID(finalCourseTitle);
            //Check Assessment Check Mark Status - Start
            Log.d("Previous Checked", subAssessmentAdapter.assessmentsPreviousChecked.toString());
            Log.d("Checked Now", subAssessmentAdapter.assessmentsChecked.toString());
            subAssessmentAdapter.assessmentsChecked.forEach((n) -> db.updateCourseInAssessments(n, currentCourseID));
            subAssessmentAdapter.assessmentsPreviousChecked.removeAll(subAssessmentAdapter.assessmentsChecked);
            ArrayList<Integer> removedAssessments = subAssessmentAdapter.assessmentsPreviousChecked;
            removedAssessments.forEach((n) -> db.updateCourseInAssessments(n, 1));
            Log.d("Find removed", removedAssessments.toString());
            //Check Assessment Check Mark Status - Start Check Mark Status - End
            //Set Course Alerts
            setCourseAlerts();
            //Set Assessment Alerts
            setAssessmentAlerts();
            //Go to Course List Activity

            Intent myIntent = new Intent(this, CourseList.class);
            startActivity(myIntent);
        }
    }
    public void cancelBtn(View view) {
        Intent myIntent = new Intent(this, CourseList.class);
        startActivity(myIntent);


    }

    public int getCurrentCourseID(String courseTitle){
        //GET Created Course's ID - START
        ArrayList<Course> coursesList = lists.getAllCourse();
        for (Course course : coursesList){
            if (course.getTitle().equals(courseTitle)){
                return course.courseID;

            }
        }
        //GET Created Course's ID - END
        return 1;
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
