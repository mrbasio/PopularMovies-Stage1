package com.example.ahmad.popularmovies01.Activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ahmad.popularmovies01.Adapters.TrailersAdapter;
import com.example.ahmad.popularmovies01.Fragments.ReviewFragment;
import com.example.ahmad.popularmovies01.NonScrollListView;
import com.example.ahmad.popularmovies01.Objects.Trailer;
import com.example.ahmad.popularmovies01.Objects.Trailers;
import com.example.ahmad.popularmovies01.R;
import com.example.ahmad.popularmovies01.Routes.PublicApiRoutes;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;


public class MovieDetail extends ActionBarActivity implements ReviewFragment.OnFragmentInteractionListener {
    ArrayList<String> youtubeKeys;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Intent intent = getIntent();
        TextView title = (TextView) findViewById(R.id.movie_title);
        title.setText(intent.getStringExtra("title"));
        TextView overview = (TextView) findViewById(R.id.movie_overview);
        overview.setText(intent.getStringExtra("overview"));
        overview.setFocusable(false);
        TextView rate = (TextView) findViewById(R.id.movie_rating);
        rate.setText(intent.getStringExtra("vote_average"));
        TextView date = (TextView) findViewById(R.id.movie_release_date);
        date.setText(intent.getStringExtra("release_date"));
        ImageView poster = (ImageView) findViewById(R.id.image_poster);
        Picasso.with(getApplication()).load(intent.getStringExtra("poster_path")).into(poster);

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FetchTrailersData fetchTrailersData = new FetchTrailersData();
        fetchTrailersData.execute(intent.getStringExtra("id"));
        //for (int i=0;i<)
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


    public void showReviews(View view) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ReviewFragment reviewFragment = new ReviewFragment();
        fragmentTransaction.replace(android.R.id.content, reviewFragment).addToBackStack("tag");
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(String id) {

    }


    public class FetchTrailersData extends AsyncTask<String, Void, List<Trailer>> {

        @Override
        protected List<Trailer> doInBackground(String... params) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("https://api.themoviedb.org/3").build();
            PublicApiRoutes publicApiRoutes = restAdapter.create(PublicApiRoutes.class);
            Trailers trailers = publicApiRoutes.TRAILERS(params[0]);
            return trailers.Trailers;
        }

        @Override
        protected void onPostExecute(final List<Trailer> arrayList) {
            NonScrollListView non_scroll_list = (NonScrollListView) findViewById(R.id.list_id_in_detail);
            TrailersAdapter trailersAdapter = new TrailersAdapter(getApplicationContext(), arrayList);
            non_scroll_list.setAdapter(trailersAdapter);
            non_scroll_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String ss = arrayList.get(position).getKey();
                    i.setData(Uri.parse(arrayList.get(position).getKey()));
                    startActivity(i);
                }
            });
            non_scroll_list.setFocusable(false);
            super.onPostExecute(arrayList);
        }
    }


}