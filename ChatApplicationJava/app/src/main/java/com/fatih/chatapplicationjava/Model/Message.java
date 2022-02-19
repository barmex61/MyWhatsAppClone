package com.fatih.chatapplicationjava.Model;

import com.google.firebase.Timestamp;

public class Message {
    public  String message,uid;
    public Long date;
    public String userName;

    public Message() {
    }

    public Message(String message, String uid, Long date) {
        this.message = message;
        this.uid = uid;
        this.date = date;
    }

    public Message(String message, String uid, Long date, String userName) {
        this.message = message;
        this.uid = uid;
        this.date = date;
        this.userName = userName;
    }
}
