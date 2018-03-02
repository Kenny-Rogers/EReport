package com.example.android.ereport.models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by krogers on 3/1/18.
 */
@Entity
public class Announcement {
    @Id
    Long id;

    private String image;
    private String title;
    private String message;
    private String date;

    public Announcement() {
    }

    public Announcement(Long id, String image, String title, String message, String date) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public Announcement(String image, String title, String message) {
        this.image = image;
        this.title = title;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
