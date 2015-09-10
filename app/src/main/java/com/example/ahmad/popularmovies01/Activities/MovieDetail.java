package com.example.ahmad.popularmovies01.Activities;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.ahmad.popularmovies01.Data.MoviesDbHelper;
import com.example.ahmad.popularmovies01.Fragments.DetailFragment;
import com.example.ahmad.popularmovies01.R;

import java.util.ArrayList;


public class MovieDetail extends ActionBarActivity {
    ArrayList<String> youtubeKeys;
    ListView listView;
    String Tag = "TAG";
    MoviesDbHelper moviesDbHelper;
    //private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DetailFragment fragment = new DetailFragment();
        setContentView(R.layout.activity_movie_detail);
        moviesDbHelper = new MoviesDbHelper(getApplicationContext());
        getSupportFragmentManager().beginTransaction().add(R.id.movie_detail_container,
                fragment, Tag).commit();

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}