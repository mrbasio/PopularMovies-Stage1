package com.example.ahmad.popularmovies01.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.example.ahmad.popularmovies01.Services.FetchMoviesService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllMovieFragment extends Fragment {
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
        Log.d("doesIt", "onCreateView  :  " + type);
        List<Movie> list = moviesDbHelper.getAllMovies(type);
//        if (list != null)
        createGrid();
//        if (!type.equals("favourite")) {
//            Intent intent = new Intent(getActivity(), FetchMoviesService.class);
//            getActivity().startService(intent);
//            createGrid();
//        }
        // this.getLoaderManager().initLoader(0, null, this);

        return root;
    }

    private void createGrid() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String type = prefs.getString("sort", getActivity().getString(R.string.popularity));
        Log.d("doesIt", "createGrid  :  " + type);
        Cursor cursor = getActivity().getContentResolver().
                query(MoviesProvider.CONTENT_URI, null, null, null, null);
        if (!type.equals(getActivity().getString(R.string.favourite))) {
            Intent intent = new Intent(getActivity(), FetchMoviesService.class);
            getActivity().startService(intent);
            gridView.setAdapter(new ImageCursorAdapter(getActivity(),
                    moviesDbHelper.getAllMoviesCursor(type)));
        } else {
            gridView.setAdapter(new ImageCursorAdapter(getActivity(), cursor));
        }
        final List<Movie> list = moviesDbHelper.getAllMovies(type);
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
    public void onResume() {
        createGrid();
        super.onResume();
    }


}