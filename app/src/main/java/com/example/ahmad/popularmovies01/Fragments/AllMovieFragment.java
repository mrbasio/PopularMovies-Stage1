package com.example.ahmad.popularmovies01.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.ahmad.popularmovies01.Activities.AllMovies;
import com.example.ahmad.popularmovies01.Activities.MovieDetail;
import com.example.ahmad.popularmovies01.Adapters.ImageCursorAdapter;
import com.example.ahmad.popularmovies01.Data.MoviesDbHelper;
import com.example.ahmad.popularmovies01.Data.MoviesProvider;
import com.example.ahmad.popularmovies01.Objects.Movie;
import com.example.ahmad.popularmovies01.R;
import com.example.ahmad.popularmovies01.TestLoader;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMovieFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Movie>> {
    MoviesDbHelper moviesDbHelper;
    GridView gridView;


    public AllMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_allmovies, container, false);
        this.gridView = (GridView) root.findViewById(R.id.gridview);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String type = prefs.getString("sort", getActivity().getString(R.string.popularity));
        moviesDbHelper = new MoviesDbHelper(getActivity());
        //moviesDbHelper.deleteAll(1);
        List<Movie> list = moviesDbHelper.getAllMovies(type);
        if (list != null)
            createGrid(list);
        if (!type.equals("favourite"))
            this.getLoaderManager().initLoader(0, null, this);

        return root;
    }

    private void createGrid(final List<Movie> list) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String type = prefs.getString("sort", getActivity().getString(R.string.popularity));
        Cursor cursor = getActivity().getContentResolver().query(MoviesProvider.CONTENT_URI, null, null, null, null);
        if (!type.equals(getActivity().getString(R.string.favourite)))
            gridView.setAdapter(new ImageCursorAdapter(getActivity(),
                    moviesDbHelper.getAllMoviesCursor(type)));
        else
            gridView.setAdapter(new ImageCursorAdapter(getActivity(), cursor));
        //gridview.setAdapter(new ImageAdapter(getApplication(), list));
        Bundle bundle = new Bundle();
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                SharedPreferences.Editor editor = getActivity().getSharedPreferences("PREF",
                        Context.MODE_PRIVATE).edit();
                editor.putString("title", list.get(position).getTitle());
                editor.putString("overview", list.get(position).getOverview());
                editor.putString("release_date", list.get(position).getRelease_date());
                editor.putString("vote_average", list.get(position).getVote_average());
                editor.putString("poster_path", list.get(position).getPoster_path());
                editor.putString("id", list.get(position).getId());
                editor.commit();

                if (AllMovies.mTwoPan) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, new DetailFragment()).commit();
                } else {
                    Intent intent = new Intent(getActivity(), MovieDetail.class);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public android.support.v4.content.Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        TestLoader testLoader = new TestLoader(getActivity());
        return testLoader;
    }


    @Override
    public void onLoadFinished(android.support.v4.content.Loader<List<Movie>> loader,
                               List<Movie> list) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String s = "http://image.tmdb.org/t/p/w500";
        String type = pref.getString("sort", getActivity().getString(R.string.popularity));


        if (list == null) {
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            //imagesList.add(s + list.get(i).getPoster_path());
            if (list.get(i).getPoster_path() == null || list.get(i).getPoster_path().equals("")) {
                list.remove(i);
                i--;
            } else {
                String temp = s + list.get(i).getPoster_path();
                list.get(i).setPoster_path(temp);
            }
        }
        boolean flag = type.equals("favourite");
        if (!flag) {
            moviesDbHelper.deleteAll(0);
            addToDB(list);
        }
        createGrid(list);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<List<Movie>> loader) {

    }

    @Override
    public void onResume() {
        this.getLoaderManager().initLoader(0, null, this);
        super.onResume();
    }

    private void addToDB(List<Movie> movies) {
        MoviesProvider moviesProvider = new MoviesProvider();
        for (int i = 0; i < movies.size(); i++) {
            moviesDbHelper.addMovie(movies.get(i), 0);
        }
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

}