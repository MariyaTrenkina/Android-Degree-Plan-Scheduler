package com.myapplication;

public class Note {
    int notesID;
    String title;
    String body;

    public Note(int notesID, String title, String body) {
        this.notesID = notesID;
        this.title = title;
        this.body = body;
    }

    public int getNotesID() {
        return notesID;
    }

    public void setNotesID(int notesID) {
        this.notesID = notesID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
