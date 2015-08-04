package com.example.ahmad.popularmovies01.Objects;

/**
 * Created by ahmad on 24/06/2015.
 */
public class Trailer {
    String name;
    String key;

    public Trailer() {

    }

    public Trailer(String name, String key) {
        this.name = name;
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        String youtube_base_url = "https://www.youtube.com/watch?v=";
        return youtube_base_url + key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
