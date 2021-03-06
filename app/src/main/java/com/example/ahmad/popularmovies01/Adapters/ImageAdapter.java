package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ahmad.popularmovies01.Objects.Movie;
import com.example.ahmad.popularmovies01.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmad on 22/06/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    List<Movie> arrayList;

    public ImageAdapter(Context c, List<Movie> arrayList) {
        mContext = c;
        this.arrayList = arrayList;
    }

    public int getCount() {
        return arrayList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            String type = prefs.getString("sort", mContext.getString(R.string.popularity));
            if (!type.equals("vote_average")) {
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                        GridView.LayoutParams.WRAP_CONTENT));
            } else {
                // I am only doing this because the images coming when asking for most voted movies
                // are not in the same size so the GridViews overlap each other
                imageView.setLayoutParams(new GridView.LayoutParams(550, 800));
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(arrayList.get(position).getPoster_path()).into(imageView);

        return imageView;
    }


}

