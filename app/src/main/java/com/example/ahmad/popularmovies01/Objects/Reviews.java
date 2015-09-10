package com.example.ahmad.popularmovies01.Objects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ahmad on 06/09/2015.
 */
public class Reviews {
    @SerializedName(value = "results")
    public List<Review> reviews;

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
