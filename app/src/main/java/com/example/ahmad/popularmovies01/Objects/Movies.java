package com.example.ahmad.popularmovies01.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmad on 23/06/2015.
 */
public class Movies {
    @SerializedName(value = "results")
    public List<Movie> Movies;

    public void setMovies(List<Movie> movies) {
        this.Movies = movies;
    }
}

