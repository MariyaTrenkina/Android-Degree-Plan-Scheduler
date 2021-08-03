package com.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class DBHelper extends SQLiteOpenHelper {
    Lists lists = new Lists();
    public DBHelper(@Nullable Context context) {
        super(context, "degreetracker.db", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("create Table Terms(termID INTEGER PRIMARY KEY, name TEXT, startDate TEXT, endDate TEXT)");
        db.execSQL("create Table Courses(courseID INTEGER PRIMARY KEY, note TEXT, termID INTEGER, title TEXT, startDate TEXT, endDate TEXT, status TEXT, courseMentorID INTEGER, startAlert INTEGER, endAlert INTEGER)");
        db.execSQL("create Table Assessments(assessmentID INTEGER PRIMARY KEY, courseID INTEGER, title TEXT, assessmentType TEXT, status TEXT, startDate TEXT, endDate TEXT, startAlert INTEGER, endAlert INTEGER)");
        db.execSQL("create Table Mentors(courseMentorID INTEGER PRIMARY KEY, name TEXT, phoneNumber TEXT, emailAddress TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE Terms");
        db.execSQL("DROP TABLE Courses");
        db.execSQL("DROP TABLE Assessments");
        db.execSQL("DROP TABLE Mentors");
        onCreate(db);
    }
//insert sql
    public Boolean insertTerm(String name, String startDate, String endDate){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("name", name);
        contentValues.put("startDate", startDate);
        contentValues.put("endDate", endDate);
        long result = db.insert( "Terms", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Boolean insertCourses(String note, int termID, String title, String startDate, String endDate, String status, int courseMentorID, int startAlert, int endAlert){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note);
        contentValues.put("termID", termID);
        contentValues.put("title", title);
        contentValues.put("startDate", startDate);
        contentValues.put("endDate", endDate);
        contentValues.put("status", status);
        contentValues.put("courseMentorID", courseMentorID);
        contentValues.put("startAlert", startAlert);
        contentValues.put("endAlert", endAlert);
        long result = db.insert( "Courses", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Boolean insertAssessment(int courseID, String title, String assessmentType, String status, String startDate, String endDate, int startAlert, int endAlert){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("courseID", courseID);
        contentValues.put("startDate", startDate);
        contentValues.put("endDate", endDate);
        contentValues.put("title", title);
        contentValues.put("assessmentType", assessmentType);
        contentValues.put("status", status);
        contentValues.put("startAlert", startAlert);
        contentValues.put("endAlert", endAlert);
        long result = db.insert( "Assessments", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }

    public Boolean insertMentor(String name, String phoneNumber, String emailAddress){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("emailAddress", emailAddress);
        long result = db.insert( "Mentors", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }

    }


//update sql
    public Boolean updateTerm(String name, String startDate, String endDate, int termID){
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues contentValues = new ContentValues();
    contentValues.put("name", name);
    contentValues.put("startDate", startDate);
    contentValues.put("endDate", endDate);
    Cursor cursor = db.rawQuery("SELECT * FROM Terms WHERE termID = ?", new String[]{String.valueOf(termID)});
    if(cursor.getCount()>0){
        long result = db.update( "Terms", contentValues,"termID = ?",new String[]{String.valueOf(termID)});
        if(result==-1){
            return false;
        }else{
            return true;
        }
        }else
        {
            return false;
        }
    }

    public Boolean updateCourses(String note, int termID, String title, String startDate, String endDate, String status, int courseMentorID,int startAlert,int endAlert, int courseID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note);
        contentValues.put("termID", termID);
        contentValues.put("title", title);
        contentValues.put("startDate", startDate);
        contentValues.put("endDate", endDate);
        contentValues.put("status", status);
        contentValues.put("courseMentorID", courseMentorID);
        contentValues.put("startAlert", startAlert);
        contentValues.put("endAlert", endAlert);
        Cursor cursor = db.rawQuery("SELECT * FROM Courses WHERE courseID = ?", new String[]{String.valueOf(courseID)});
        if(cursor.getCount()>0){
            long result = db.update( "Courses", contentValues,"courseID = ?",new String[]{String.valueOf(courseID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }
    //Update Course's Term - START
    public Boolean updateTermInCourses( int courseID, int termID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("termID", termID);
        Cursor cursor = db.rawQuery("SELECT * FROM Courses WHERE courseID = ?", new String[]{String.valueOf(courseID)});
        if(cursor.getCount()>0){
            long result = db.update( "Courses", contentValues,"courseID = ?",new String[]{String.valueOf(courseID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }
    //Update Course's Term - END
    //Update Course's MentorID - START
    public Boolean updateMentorIDInCourses( int courseID, int courseMentorID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("courseMentorID", courseMentorID);
        Cursor cursor = db.rawQuery("SELECT * FROM Courses WHERE courseID = ?", new String[]{String.valueOf(courseID)});
        if(cursor.getCount()>0){
            long result = db.update( "Courses", contentValues,"courseID = ?",new String[]{String.valueOf(courseID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }
    //Update Course's MentorID - END
    //Update Assessment's Course - START
    public Boolean updateCourseInAssessments( int assessmentID, int courseID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("courseID", courseID);
        Cursor cursor = db.rawQuery("SELECT * FROM Assessments WHERE assessmentID = ?", new String[]{String.valueOf(assessmentID)});
        if(cursor.getCount()>0){
            long result = db.update( "Assessments", contentValues,"assessmentID = ?",new String[]{String.valueOf(assessmentID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }
    //Update Assessment's Course - END
    //Update course's mentorID - START
    public Boolean updateCourseMentorIDInCourses( int courseMentorID, int courseID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("courseMentorID", courseMentorID);
        Cursor cursor = db.rawQuery("SELECT * FROM Courses WHERE courseID = ?", new String[]{String.valueOf(courseID)});
        if(cursor.getCount()>0){
            long result = db.update( "Courses", contentValues,"courseID = ?",new String[]{String.valueOf(courseID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }
    //Update Course's mentorID - END



    public Boolean updateAssessment(int assessmentID, int courseID, String title, String assessmentType, String status, String startDate, String endDate, int startAlert, int endAlert){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("assessmentID", assessmentID);
        contentValues.put("courseID", courseID);
        contentValues.put("startDate", startDate);
        contentValues.put("endDate", endDate);
        contentValues.put("title", title);
        contentValues.put("assessmentType", assessmentType);
        contentValues.put("status", status);
        contentValues.put("startAlert", startAlert);
        contentValues.put("endAlert", endAlert);
        Cursor cursor = db.rawQuery("SELECT * FROM Assessments WHERE assessmentID = ?", new String[]{String.valueOf(assessmentID)});
        if(cursor.getCount()>0){
            long result = db.update( "Assessments", contentValues, "assessmentID = ?",new String[]{String.valueOf(assessmentID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }

    public Boolean updateMentor(String name, String phoneNumber, String emailAddress, int courseMentorID){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phoneNumber", phoneNumber);
        contentValues.put("emailAddress", emailAddress);
        Cursor cursor = db.rawQuery("SELECT * FROM Mentors WHERE courseMentorID = ?", new String[]{String.valueOf(courseMentorID)});
        if(cursor.getCount()>0){
            long result = db.update( "Mentors", contentValues,"courseMentorID = ?",new String[]{String.valueOf(courseMentorID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }




//delete from sql
    public Boolean deleteTerm(int termID){
    SQLiteDatabase db = this.getWritableDatabase();
    Cursor cursor = db.rawQuery("SELECT * FROM Terms WHERE termID = ?", new String[]{String.valueOf(termID)});
    if(cursor.getCount()>0){
        long result = db.delete( "Terms","termID = ?",new String[]{String.valueOf(termID)});
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }else
    {
        return false;
    }
}

    public Boolean deleteCourses(int courseID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Courses WHERE courseID = ?", new String[]{String.valueOf(courseID)});
        if(cursor.getCount()>0){
            long result = db.delete( "Courses","courseID = ?",new String[]{String.valueOf(courseID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }

    public Boolean deleteAssessment(int assessmentID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Assessments WHERE assessmentID = ?", new String[]{String.valueOf(assessmentID)});
        if(cursor.getCount()>0){
            long result = db.delete( "Assessments","assessmentID = ?",new String[]{String.valueOf(assessmentID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }

    public Boolean deleteMentor(int courseMentorID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Mentors WHERE courseMentorID = ?", new String[]{String.valueOf(courseMentorID)});
        if(cursor.getCount()>0){
            long result = db.delete( "Mentors","courseMentorID = ?",new String[]{String.valueOf(courseMentorID)});
            if(result==-1){
                return false;
            }else{
                return true;
            }
        }else
        {
            return false;
        }
    }


//select or getting all from sql

    public Cursor getAllTerms (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Terms ORDER BY startDate", null);


        return cursor;

    }

    public Cursor getAllCourses (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Courses", null);
        return cursor;

    }

    public Cursor getAllAssessments (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Assessments", null);
        return cursor;

    }

    public Cursor getAllMentors (){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Mentors", null);
        return cursor;

    }


    public void clearTermsTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Terms");

    }
    public void clearCoursesTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Courses");

    }
    public void clearAssessmentsTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Assessments");

    }
    public void clearMentorsTable (){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Mentors");

    }

    public void clearAllTables (){
        clearTermsTable();
        clearCoursesTable();
        clearAssessmentsTable();
        clearMentorsTable();
    }
}
