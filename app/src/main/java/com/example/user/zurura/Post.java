package com.example.user.zurura;

/**
 * Created by paul on 15/06/2017.
 */

public class Post {

    public String description;
    public String location;
    public String timestamp;
    public String photoUrl;
    public String userID;

    public Post(String s, Object o, String uid) {
    }

    public Post(String description, String location, String timestamp, String photoUrl, String userID) {
        this.description = description;
        this.location = location;
        this.timestamp = timestamp;
        this.photoUrl = photoUrl;
        this.userID = userID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
