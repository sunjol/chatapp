package com.example.chatactivity2;

import android.widget.ImageView;

public class user {
    private String id;
    private String username;
    private String image;

    public user(String id, String username, String image) {
        this.id = id;
        this.username = username;
        this.image = image;
    }

    public user() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
