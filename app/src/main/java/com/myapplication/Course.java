package com.myapplication;

public class Course {
    int  courseID;
    String note;
    int termID;
    String title;
    String startDate;
    String endDate;
    String status;
    int courseMentorID;
    int startAlert;
    int endAlert;

    public Course(int courseID, String note, int termID, String title, String startDate, String endDate, String status, int courseMentorID, int startAlert, int endAlert) {
        this.courseID = courseID;
        this.note = note;
        this.termID = termID;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.courseMentorID = courseMentorID;
        this.startAlert = startAlert;
        this.endAlert = endAlert;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getNote() {
        return note;
    }

    public void setNoteID(int noteID) {
        this.note = note;
    }

    public int getTermID() {
        return termID;
    }

    public void setTermID(int termID) {
        this.termID = termID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCourseMentorID() {
        return courseMentorID;
    }

    public void setCourseMentorID(int courseMentorID) {
        this.courseMentorID = courseMentorID;
    }

    public int getStartAlert() {
        return startAlert;
    }

    public void setStartAlert(int startAlert) {
        this.startAlert = startAlert;
    }

    public int getEndAlert() {
        return endAlert;
    }

    public void setEndAlert(int endAlert) {
        this.endAlert = endAlert;
    }
}
