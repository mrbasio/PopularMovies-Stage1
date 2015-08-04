package com.example.ahmad.popularmovies01.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.ahmad.popularmovies01.Adapters.ImageAdapter;
import com.example.ahmad.popularmovies01.Objects.Movie;
import com.example.ahmad.popularmovies01.Objects.Movies;
import com.example.ahmad.popularmovies01.R;
import com.example.ahmad.popularmovies01.Routes.PublicApiRoutes;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import retrofit.RestAdapter;


public class AllMovies extends ActionBarActivity {
    ListView listView;
    ArrayList<Hashtable<String, String>> hashtableArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_movies);
        Log.e("Life", "onCreate");
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_movies, menu);
        return true;
    }

    @Override
    protected void onResume() {
        Log.e("Life", "onResume");
        FetchMovies fetchMovies = new FetchMovies();
        fetchMovies.execute();
        super.onResume();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMovies extends AsyncTask<Void, Void, List<Movie>> {

        @Override
        protected List<Movie> doInBackground(Void... params) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint("https://api.themoviedb.org/3").build();
            PublicApiRoutes publicApiRoutes = restAdapter.create(PublicApiRoutes.class);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String type = prefs.getString("sort", getString(R.string.popularity));
            Movies movies = publicApiRoutes.MOVIE_LIST(type + ".desc");
            return movies.Movies;

        }

        @Override
        protected void onPostExecute(final List<Movie> list) {
            ArrayList<String> imagesList = new ArrayList<>();
            final String s = "http://image.tmdb.org/t/p/w500/";
            for (int i = 0; i < list.size(); i++) {
                imagesList.add(s + list.get(i).getPoster_path());
            }
            GridView gridview = (GridView) findViewById(R.id.gridview);
            gridview.setAdapter(new ImageAdapter(getApplication(), imagesList));
            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent intent = new Intent(AllMovies.this, MovieDetail.class);
                    intent.putExtra("title", list.get(position).getTitle());
                    intent.putExtra("overview", list.get(position).getOverview());
                    intent.putExtra("release_date", list.get(position).getRelease_date());
                    intent.putExtra("vote_average", list.get(position).getVote_average());
                    intent.putExtra("poster_path", s + list.get(position).getPoster_path());
                    intent.putExtra("id", list.get(position).getId());
                    startActivity(intent);
                }
            });
            super.onPostExecute(list);
        }
    }
    /*    @Override
    protected void onStart() {
        super.onStart();
        Log.e("Life", "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("Life", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("Life", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("Life", "onDestroy");
    }*/
}
