package com.joysistvi.CyberAccess.model;

public class Feedback {

    private int id;
    private String comment;
    private int userId;

    public Feedback(int id, String comment, int userId) {
        this.id = id;
        this.comment = comment;
        this.userId = userId;
    }

    public Feedback(String comment, int userId) {
        this.comment = comment;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}