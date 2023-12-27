package com.ExamPort.ExamServer.Entities;

public class JwtRequest {
    String username;
    String password;

    public JwtRequest() {

    }

    public JwtRequest(String username,String password) {
        this.username = username;
        this.password=  password;
    }

    public String getUsername() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
