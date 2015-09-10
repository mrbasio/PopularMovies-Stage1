package com.example.ahmad.popularmovies01;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ahmad.popularmovies01.Objects.Review;
import com.example.ahmad.popularmovies01.Objects.Reviews;
import com.example.ahmad.popularmovies01.Routes.PublicApiRoutes;

import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by ahmad on 06/09/2015.
 */
public class ReviewLoader extends AsyncTaskLoader<List<Review>> {
    public ReviewLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        super.onStartLoading();
    }

    @Override
    public List<Review> loadInBackground() {

        SharedPreferences sharedPreferences = getContext()
                .getSharedPreferences("PREF", Context.MODE_PRIVATE);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api.themoviedb.org/3").build();
        String s = sharedPreferences.getString("id", "null");
        PublicApiRoutes publicApiRoutes = restAdapter.create(PublicApiRoutes.class);
        Reviews reviews = publicApiRoutes.REVIEWS(sharedPreferences.getString("id", "null"));
        if (reviews.reviews.size()==0){
            //List<Review> reviews1 = new ArrayList<>();
            reviews.reviews.add(new Review("", 0, "No Reviews for This movies Yet", ""));
        }
        return reviews.reviews;
    }
}
