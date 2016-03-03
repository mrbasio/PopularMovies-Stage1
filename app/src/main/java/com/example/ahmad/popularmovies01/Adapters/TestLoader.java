package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ahmad.popularmovies01.Data.MoviesDbHelper;
import com.example.ahmad.popularmovies01.Objects.Movie;
import com.example.ahmad.popularmovies01.Objects.Movies;
import com.example.ahmad.popularmovies01.R;
import com.example.ahmad.popularmovies01.Routes.PublicApiRoutes;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;


public class TestLoader extends AsyncTaskLoader<List<Movie>> {// implements BroadcastReceiver {
    public static final String loaderString = "wateve";
    Context context;

    public TestLoader(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onStartLoading() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(loaderString);
        forceLoad();
        super.onStartLoading();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }

    @Override
    public List<Movie> loadInBackground() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String type = prefs.getString("sort", context.getString(R.string.popularity));
        List<Movie> movies = new ArrayList<Movie>();
        if (type.equals("favourite")) {
            MoviesDbHelper moviesDbHelper;
            moviesDbHelper = new MoviesDbHelper(context);
            movies = moviesDbHelper.getAllMovies(type);
        } else if (isNetworkAvailable()){
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.themoviedb.org/3").build();
            PublicApiRoutes publicApiRoutes = restAdapter.create(PublicApiRoutes.class);
            Movies m = publicApiRoutes.MOVIE_LIST(type + ".desc");
            movies = m.Movies;
        }
        return movies;

    }

    @Override
    public void deliverResult(List<Movie> data) {
        if (isStarted())
            super.deliverResult(data);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean flag = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        return flag;
    }

}
