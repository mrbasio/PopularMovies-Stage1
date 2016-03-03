package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ahmad.popularmovies01.Objects.Trailer;
import com.example.ahmad.popularmovies01.Objects.Trailers;
import com.example.ahmad.popularmovies01.Routes.PublicApiRoutes;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by ahmad on 08/09/2015.
 */
public class TrailerLoader extends AsyncTaskLoader<List<Trailer>> {
    Context context;
    String s;

    public TrailerLoader(Context context, String s) {
        super(context);
        this.context = context;
        this.s = s;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public List<Trailer> loadInBackground() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.themoviedb.org/3").build();
        PublicApiRoutes publicApiRoutes = restAdapter.create(PublicApiRoutes.class);
        Trailers trailers = publicApiRoutes.TRAILERS(s);
        return trailers.Trailers;
    }
}
