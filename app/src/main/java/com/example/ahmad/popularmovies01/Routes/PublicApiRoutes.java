package com.example.ahmad.popularmovies01.Routes;

import com.example.ahmad.popularmovies01.Objects.Movies;
import com.example.ahmad.popularmovies01.Objects.Reviews;
import com.example.ahmad.popularmovies01.Objects.Trailers;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by ahmad on 23/06/2015.
 */
public interface PublicApiRoutes {
    @GET("/discover/movie?api_key=fa324076f75b84a267d88a4b41ea2ed8")
    Movies MOVIE_LIST(@Query("sort_by") String sort_by);
    //void getMovies(@Path("sort_by") String sort_by, Callback<ArrayList<Movie>>)

    @GET("/movie/{id}/videos?api_key=fa324076f75b84a267d88a4b41ea2ed8")
    Trailers TRAILERS(@Path("id") String id);

    @GET("/movie/{id}/reviews?api_key=fa324076f75b84a267d88a4b41ea2ed8")
    Reviews REVIEWS(@Path("id") String id);
}
