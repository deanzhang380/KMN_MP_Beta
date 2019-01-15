package com.example.rai.kmn_mp_beta;

import android.graphics.Bitmap;
import android.media.Image;
import android.provider.MediaStore;

import java.util.ArrayList;

public class PlayList {
    private Bitmap image;
    private String name;

    public ArrayList<Music> getMusic() {
        return music;
    }

    public void setMusic(ArrayList<Music> music) {
        this.music = music;
    }

    private ArrayList<Music> music = new ArrayList<Music>();

    public PlayList() {
    }

    public PlayList(Bitmap image, String name, ArrayList<Music> music) {
        this.image = image;
        this.name = name;
        this.music = music;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayList(Bitmap image, String name) {

        this.image = image;
        this.name = name;
    }
}
