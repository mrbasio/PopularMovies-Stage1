package com.example.ahmad.popularmovies01.Fragments;


import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmad.popularmovies01.Adapters.TrailersAdapter;
import com.example.ahmad.popularmovies01.Data.MoviesDbHelper;
import com.example.ahmad.popularmovies01.Data.MoviesProvider;
import com.example.ahmad.popularmovies01.MyDialogReviewFragment;
import com.example.ahmad.popularmovies01.NonScrollListView;
import com.example.ahmad.popularmovies01.Objects.Movie;
import com.example.ahmad.popularmovies01.Objects.Trailer;
import com.example.ahmad.popularmovies01.PicDownloadLoder;
import com.example.ahmad.popularmovies01.R;
import com.example.ahmad.popularmovies01.TrailerLoader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    NonScrollListView nonScrollListView;
    CheckBox box;
    Button button;
    MoviesDbHelper moviesDbHelper;
    private ShareActionProvider mShareActionProvider;
    String share_content = "No trailers :(";


    public DetailFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_detail, container, false);
        if (isNetworkAvailable())
            getLoaderManager().initLoader(10, null, listLoaderCallbacks);

        this.nonScrollListView = (NonScrollListView) root.findViewById(R.id.list_id_in_detail);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);

        // Intent intent = getIntent();
        TextView title = (TextView) root.findViewById(R.id.movie_title);
        title.setText(sharedPreferences.getString("title", "null"));
        TextView overview = (TextView) root.findViewById(R.id.movie_overview);
        overview.setText(sharedPreferences.getString("overview", "null"));
        overview.setFocusable(false);
        TextView rate = (TextView) root.findViewById(R.id.movie_rating);
        rate.setText(sharedPreferences.getString("vote_average", "null"));
        TextView date = (TextView) root.findViewById(R.id.movie_release_date);
        date.setText(sharedPreferences.getString("release_date", "null"));
        ImageView poster = (ImageView) root.findViewById(R.id.image_poster);
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final String type = prefs.getString("sort", getActivity().getString(R.string.popularity));
        if (type.equals("favourite")) {
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                    + "/storage/emulated/0" + "/popularMovies" + "/"
                    + sharedPreferences.getString("id", "any") + ".jpg");
            poster.setImageBitmap(bitmap);
        } else
            Picasso.with(getActivity()).load(sharedPreferences.getString("poster_path", "null")).into(poster);
        box = (CheckBox) root.findViewById(R.id.star);
        button = (Button) root.findViewById(R.id.review_button);
        moviesDbHelper = new MoviesDbHelper(getActivity());
        if (moviesDbHelper.ifExcite(sharedPreferences.getString("id", "null"))) {
            box.setChecked(true);
        }


        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    final String type = prefs.getString("sort", getActivity().getString(R.string.popularity));
                    if (!type.equals("favourite")) {
                        Movie movie = moviesDbHelper
                                .getMovie(sharedPreferences.getString("id", "null"), 0);

                        ContentValues values = new ContentValues();
                        values.put(MoviesDbHelper.COLUMN_MOVIE_STRING_ID, movie.getId());
                        values.put(MoviesDbHelper.COLUMN_MOVIE_OVERVIEW, movie.getOverview());
                        values.put(MoviesDbHelper.COLUMN_MOVIE_RELEASE_DATE, movie.getRelease_date());
                        values.put(MoviesDbHelper.COLUMN_MOVIE_IMAGE_POSTER, movie.getPoster_path());
                        values.put(MoviesDbHelper.COLUMN_MOVIE_TITLE, movie.getTitle());
                        values.put(MoviesDbHelper.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVote_average());

                        getActivity().getContentResolver().insert(MoviesProvider.CONTENT_URI, values);

                        //   moviesDbHelper.addMovie(moviesDbHelper.getMovie(sharedPreferences.getString("id", "null"), 0), 1);

                        getLoaderManager().initLoader(20, null, voidLoaderCallbacks);
                    } else {
                        box.setChecked(false);
                    }
                } else {
                    moviesDbHelper.deleteMovie(sharedPreferences.getString("id", "null"));
                    File file = new File(Environment.getExternalStorageDirectory()
                            + "/storage/emulated/0" + "/popularMovies" + "/"
                            + sharedPreferences.getString("id", "null") + ".jpg");

                    boolean deleted = file.delete();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable()) {
                    DialogFragment newFragment = MyDialogReviewFragment.newInstance();
                    newFragment.show(getActivity().getSupportFragmentManager(), "dialog");
                }
            }
        });


        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (android.support.v7.widget.ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREF", Context.MODE_PRIVATE);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Checkout the trailer for "+ sharedPreferences.getString("Share", "No trailers"));
        mShareActionProvider.setShareIntent(shareIntent);

        super.onCreateOptionsMenu(menu, inflater);
    }





    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean flag = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!flag) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("No InterNet")
                    .setMessage("Check your interNet connection")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
        return flag;
    }

    private LoaderManager.LoaderCallbacks<List<Trailer>> listLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Trailer>>() {
        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            SharedPreferences sharedPreferences = getActivity()
                    .getSharedPreferences("PREF", Context.MODE_PRIVATE);
            TrailerLoader trailerLoader = new TrailerLoader(getActivity(),
                    sharedPreferences.getString("id", null));
            return trailerLoader;
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, final List<Trailer> list) {
            TrailersAdapter trailersAdapter = new TrailersAdapter(getActivity(), list);
            SharedPreferences.Editor editor = getActivity()
                    .getSharedPreferences("PREF", Context.MODE_PRIVATE).edit();
            if (list.size() >= 1) {
                editor.putString("Share", list.get(0).getKey());
                editor.commit();
            }
            nonScrollListView.setAdapter(trailersAdapter);
            nonScrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    String ss = list.get(position).getKey();
                    i.setData(Uri.parse(list.get(position).getKey()));
                    startActivity(i);
                }
            });
            nonScrollListView.setFocusable(false);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Void> voidLoaderCallbacks = new LoaderManager.LoaderCallbacks<Void>() {
        @Override
        public Loader<Void> onCreateLoader(int id, Bundle args) {
            PicDownloadLoder picDownloadLoder = new PicDownloadLoder(getActivity());
            return picDownloadLoder;
        }

        @Override
        public void onLoadFinished(Loader<Void> loader, Void data) {

        }

        @Override
        public void onLoaderReset(Loader<Void> loader) {

        }
    };
}
