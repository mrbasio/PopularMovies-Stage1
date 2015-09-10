package com.example.ahmad.popularmovies01.Objects;

/**
 * Created by ahmad on 24/06/2015.
 */
public class Review {
    String author;
    String content;
    String url;

    public Review() {

    }

    public Review(String author, int id, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
