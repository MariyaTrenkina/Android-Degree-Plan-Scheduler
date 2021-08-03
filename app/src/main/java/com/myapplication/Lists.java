package com.myapplication;

import android.database.Cursor;

import java.util.ArrayList;

public class Lists {

    static ArrayList<Term> termList = new ArrayList<Term>();
    static ArrayList<Mentor> mentorList = new ArrayList<Mentor>();
    static ArrayList<Assessment> assessmentList = new ArrayList<Assessment>();
    static ArrayList<Course> courseList = new ArrayList<Course>();

   //term
    public void addTerm(Term term){
        termList.add(term);
    }

    public ArrayList getAllTerms(){
       return termList;
    }

    public void removeTerm(Term term){
        termList.remove(term);
    }

    public void clearTerm(){
        termList.clear();
    }


    //mentor
    public void addMentor(Mentor mentor){
        mentorList.add(mentor);
    }

    public ArrayList getAllMentor(){
        return mentorList;
    }

    public void removeMentor(Mentor mentor){
        mentorList.remove(mentor);
    }

    public void clearMentor(){
        mentorList.clear();
    }

    //assesment
    public void addAssessment(Assessment assessment){
        assessmentList.add(assessment);
    }

    public ArrayList getAllAssessment(){
        return assessmentList;
    }

    public void removeAssessment(Assessment assessment){
        assessmentList.remove(assessment);
    }

    public void clearAssessment(){
        assessmentList.clear();
    }



    //course
    public void addCourse(Course course){
        courseList.add(course);
    }

    public ArrayList getAllCourse(){
        return courseList;
    }

    public void removeCourse(Course course){
        courseList.remove(course);
    }

    public void clearCourse(){
        courseList.clear();
    }
    public void clearAllLists(){

        clearTerm();
        clearCourse();
        clearAssessment();
        clearMentor();

    }
}
