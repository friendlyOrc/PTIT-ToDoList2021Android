package com.example.todolist.model;

public class Article {
    private String url;
    private String title;
    private String imgUrl;
    private String time;

    public Article() {
    }

    public Article(String url, String title, String imgUrl, String time) {
        this.url = url;
        this.title = title;
        this.imgUrl = imgUrl;
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
