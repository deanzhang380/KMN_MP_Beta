package com.example.rai.kmn_mp_beta;

import android.graphics.Bitmap;

public class Music {
    private String name;
    private String path;
    private Bitmap picture;
    private String Artist;

    public Music(String name, String path, Bitmap picture) {
        this.name = name;
        this.path = path;
        this.picture = picture;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    private int Duration;

    public Music(String name, String path, Bitmap picture, String artist, int duration) {
        this.name = name;
        this.path = path;
        this.picture = picture;
        Artist = artist;
        Duration = duration;
    }

    public Music(String name, String path, Bitmap picture, String artist) {
        this.name = name;
        this.path = path;
        this.picture = picture;
        Artist = artist;
    }

    public String getArtist() {
        return Artist;
    }

    public void setArtist(String artist) {
        Artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }
}
