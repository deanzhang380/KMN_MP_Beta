package com.example.rai.kmn_mp_beta;

/**
 * Created by KhoaPhamPC on 18/10/2017.
 */

public class Video {
    private int Id;
    private String Title;
    private String UrlImage;
    private String UrlVideo;

    public Video(int id, String title, String urlImage, String urlVideo) {
        Id = id;
        Title = title;
        UrlImage = urlImage;
        UrlVideo = urlVideo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getUrlImage() {
        return UrlImage;
    }

    public void setUrlImage(String urlImage) {
        UrlImage = urlImage;
    }

    public String getUrlVideo() {
        return UrlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        UrlVideo = urlVideo;
    }
}
