package com.myapplication;

public class Mentor {
    int courseMentorID;
    String name;
    String phoneNumber;
    String emailAddress;

    public Mentor(int courseMentorID, String name, String phoneNumber, String emailAddress) {
        this.courseMentorID = courseMentorID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
    }

    public int getCourseMentorID() {
        return courseMentorID;
    }

    public void setCourseMentorID(int courseMentorID) {
        this.courseMentorID = courseMentorID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
