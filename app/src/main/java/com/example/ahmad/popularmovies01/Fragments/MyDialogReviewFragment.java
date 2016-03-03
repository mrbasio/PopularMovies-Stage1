package com.example.ahmad.popularmovies01.Fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ahmad.popularmovies01.Adapters.ReviewAdapter;
import com.example.ahmad.popularmovies01.Adapters.ReviewLoader;
import com.example.ahmad.popularmovies01.Objects.Review;
import com.example.ahmad.popularmovies01.R;

import java.util.List;

/**
 * Created by ahmad on 06/09/2015.
 */
public class MyDialogReviewFragment extends DialogFragment implements LoaderManager.LoaderCallbacks<List<Review>> {
    ListView listView;


    public static MyDialogReviewFragment newInstance() {
        return new MyDialogReviewFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_reviews, container, false);
        listView = (ListView) root.findViewById(R.id.reviews_list_view);
        this.getLoaderManager().initLoader(1, null, this);

        return root;
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        ReviewLoader loader = new ReviewLoader(getActivity());
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> list) {
        listView.setAdapter(new ReviewAdapter(getActivity(), list));
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {

    }

}
