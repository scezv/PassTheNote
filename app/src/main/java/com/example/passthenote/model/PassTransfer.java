package com.example.passthenote.model;

public class PassTransfer {
    private String platform;
    private String password;

    public PassTransfer(){
        // empty constructor needed
    }

    public PassTransfer(String platform, String password){
        this.platform = platform;
        this.password = password;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
