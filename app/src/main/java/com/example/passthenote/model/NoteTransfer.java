package com.example.passthenote.model;

public class NoteTransfer {
    private String title;
    private String description;
    private int priority;

    public NoteTransfer(){
        // empty constructor needed
    }

    public NoteTransfer(String title, String description, int priority){
        this.title = title;
        this.description = description;
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
