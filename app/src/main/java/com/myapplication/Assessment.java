package com.myapplication;

public class Assessment {
    int assessmentID;
    int courseID;
    String title;
    String assessmentType;
    String status;
    String startDate;
    String endDate;
    int startAlert;
    int endAlert;

    public Assessment(int assessmentID, int courseID, String title, String assessmentType, String status, String startDate, String endDate, int startAlert, int endAlert) {
        this.assessmentID = assessmentID;
        this.courseID = courseID;
        this.title = title;
        this.assessmentType = assessmentType;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startAlert = startAlert;
        this.endAlert = endAlert;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
