package com.example.ahmad.popularmovies01.Objects;

/**
 * Created by ahmad on 23/06/2015.
 */
public class Movie {
    String title;
    String overview;
    String release_date;
    String vote_average;
    String poster_path;
    String id;
    int db_id;
    final String s = "http://image.tmdb.org/t/p/w500/";

    public Movie(String id, String overview, String release_date, String poster_path, String title,
                 String vote_average) {
        this.id = id;
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.title = title;
    }

    public Movie(String overview, String release_date, String poster_path, String title,
                 String vote_average) {
        this.overview = overview;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.vote_average = vote_average;
        this.title = title;
    }

    public Movie() {

    }

    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {

        this.poster_path = s + poster_path;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
