package com.fatih.chatapplicationjava.Model;

public class User {
    public String userName,password,email,photo,uid,phone,lastMessage;

    public User() {
    }

    public User(String userName, String password, String email) {
        this.userName = userName;
        this.password = password;
        this.email = email;
    }

    public User(String userName, String password, String email, String uid) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.uid = uid;
    }

    public User(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public User(String userName, String password, String email, String photo, String uid, String phone, String lastMessage) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.photo = photo;
        this.uid = uid;
        this.phone = phone;
        this.lastMessage = lastMessage;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setStatus(String phone) {
        this.phone = phone;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoto() {
        return photo;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return phone;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
