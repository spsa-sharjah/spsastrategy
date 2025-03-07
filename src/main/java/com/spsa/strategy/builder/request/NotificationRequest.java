package com.spsa.strategy.builder.request;

public class NotificationRequest {
    private String title;
    private String body;
    private String token; //This is the device token

    //Getters and Setters
    public String getTitle() { return title;}
    public void setTitle(String title) {this.title = title;}
    public String getBody() {return body;}
    public void setBody(String body) {this.body = body;}
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}
}
