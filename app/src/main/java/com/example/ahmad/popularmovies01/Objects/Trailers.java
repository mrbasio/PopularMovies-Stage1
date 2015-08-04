package com.example.ahmad.popularmovies01.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmad on 24/06/2015.
 */
public class Trailers {
    @SerializedName(value = "results")
    public List<Trailer> Trailers;

    public void setMovies(List<Trailer> trailers) {
        this.Trailers = trailers;
    }
}

