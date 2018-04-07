package com.example.android.ereport.models;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by krogers on 4/5/18.
 */
@Entity
public class Secretariat {
    @Id
    Long id;

    private String type;
    private String region;
    private String name;
    private String rep_id;
    private String lat;
    private String lng;
    private String date_published;

    public Secretariat() {
    }

    public Secretariat(Long id, String type, String region, String name, String rep_id, String lat, String lng, String date_published) {
        this.id = id;
        this.type = type;
        this.region = region;
        this.name = name;
        this.rep_id = rep_id;
        this.lat = lat;
        this.lng = lng;
        this.date_published = date_published;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRep_id() {
        return rep_id;
    }

    public void setRep_id(String rep_id) {
        this.rep_id = rep_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDate_published() {
        return date_published;
    }

    public void setDate_published(String date_published) {
        this.date_published = date_published;
    }
}
