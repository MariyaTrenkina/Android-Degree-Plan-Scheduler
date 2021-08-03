package com.myapplication;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    DBHelper db = new DBHelper(this);
    Lists lists = new Lists();
static int initDB = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_home);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
if (initDB==0){
    db.clearAllTables();
    lists.clearAllLists();
    db.insertTerm("NOT_ASSIGNED","NULL", "NULL");
    db.insertCourses("NOTES",1,"NOT_ASSIGNED","NOT_ASSIGNED","NOT_ASSIGNED","NOT_ASSIGNED",1,0,0);
    db.insertMentor("NOT_ASSIGNED","NOT_ASSIGNED","NOT_ASSIGNED");
    db.insertAssessment(1,"NOT_ASSIGNED","NOT_ASSIGNED","NOT_ASSIGNED","NOT_ASSIGNED","NOT_ASSIGNED",0,0);
    initDB = 1;
}
        //--Menu Nav Start--
        setCourseAlerts();
        requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.homeBtn);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homeBtn:
                        break;
                    case R.id.termsBtn:
                        Intent termsList = new Intent(MainActivity.this,TermList.class);
                        startActivity(termsList);
                        break;
                    case R.id.coursesBtn:
                        Intent coursesList = new Intent(MainActivity.this,CourseList.class);
                        startActivity(coursesList);
                        break;
                    case R.id.assessmentsBtn:
                        Intent assessmentsList = new Intent(MainActivity.this,AssessmentList.class);
                        startActivity(assessmentsList);
                        break;
                    case R.id.mentorsBtn:
                        Intent mentorsList = new Intent(MainActivity.this,MentorList.class);
                        startActivity(mentorsList);
                        break;
                }
                    return false;

            }
        });
     // --Menu Nav End--



    }

    public void goToViewAllTermsBtn(View view) {
        ArrayList termList = lists.getAllTerms();


        //Switch to TermList Activity
        Intent myIntent = new Intent(this, TermList.class);
        startActivity(myIntent);

    }


    public void goToViewAllCoursesBtn(View view) {
        //Switch to CourseList Activity
        Intent myIntent = new Intent(this, CourseList.class);
        startActivity(myIntent);
    }

    public void goToViewAllAssessmentsBtn(View view) {
        //Switch to AssessmentList Activity
        Intent myIntent = new Intent(this, AssessmentList.class);
        startActivity(myIntent);
    }

    public void goToViewAllMentorsBtn(View view) {
        //Switch to MentorList Activity
        Intent myIntent = new Intent(this, MentorList.class);
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


