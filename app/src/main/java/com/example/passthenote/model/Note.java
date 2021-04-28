package com.example.passthenote.model;

public class Note {
    private String title;
    private String content;
    public Note(){

    }
    public Note(String content, String title){
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        System.out.println(title);
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        System.out.println(content);
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
