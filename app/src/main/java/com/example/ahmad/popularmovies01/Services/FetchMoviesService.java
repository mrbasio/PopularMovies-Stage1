package com.example.ahmad.popularmovies01.Services;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.util.Log;

import com.example.ahmad.popularmovies01.Data.MoviesDbHelper;
import com.example.ahmad.popularmovies01.Data.MoviesProvider;
import com.example.ahmad.popularmovies01.Objects.Movie;
import com.example.ahmad.popularmovies01.Objects.Movies;
import com.example.ahmad.popularmovies01.R;
import com.example.ahmad.popularmovies01.Routes.PublicApiRoutes;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by ahmad on 20/09/2015.
 */
public class FetchMoviesService extends IntentService {
    MoviesDbHelper moviesDbHelper;


    public FetchMoviesService() {
        super("FetchMoviesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext());
        String type = prefs.getString("sort", getApplicationContext().
                getString(R.string.popularity));
        Log.d("doesIt", "onHandleIntent  :  " + type);
        List<Movie> movies = new ArrayList<Movie>();
        moviesDbHelper = new MoviesDbHelper(getApplicationContext());
        if (type.equals("favourite")) {
            MoviesDbHelper moviesDbHelper;
            moviesDbHelper = new MoviesDbHelper(getApplicationContext());
            movies = moviesDbHelper.getAllMovies(type);
        } else if (isNetworkAvailable()) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.themoviedb.org/3").build();
            PublicApiRoutes publicApiRoutes = restAdapter.create(PublicApiRoutes.class);
            Movies m = publicApiRoutes.MOVIE_LIST(type + ".desc");
            movies = m.Movies;
            Log.d("doesIt", "onHandleIntent  :  " + movies.get(0).getTitle());
        }
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String s = "http://image.tmdb.org/t/p/w500";


        if (movies == null) {
            return;
        }

        for (int i = 0; i < movies.size(); i++) {
            //imagesList.add(s + list.get(i).getPoster_path());
            if (movies.get(i).getPoster_path() == null || movies.get(i).getPoster_path().equals("")) {
                movies.remove(i);
                i--;
            } else {
                String temp = s + movies.get(i).getPoster_path();
                movies.get(i).setPoster_path(temp);
            }
        }
        boolean flag = type.equals("favourite");
        if (!flag) {
            moviesDbHelper.deleteAll(0);
            addToDB(movies);
        }
    }


    private void addToDB(List<Movie> movies) {
        MoviesProvider moviesProvider = new MoviesProvider();
        for (int i = 0; i < movies.size(); i++) {
            moviesDbHelper.addMovie(movies.get(i), 0);
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean flag = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return flag;
    }

    static public class AlarmReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }
}
