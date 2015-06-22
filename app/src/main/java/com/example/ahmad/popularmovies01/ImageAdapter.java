package com.example.ahmad.popularmovies01;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by ahmad on 22/06/2015.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    ArrayList<String> arrayList;

    public ImageAdapter(Context c, ArrayList<String> arrayList) {
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
            if (type.equals("popularity")) {
                imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                        GridView.LayoutParams.WRAP_CONTENT));
            } else {
                imageView.setLayoutParams(new GridView.LayoutParams(550, 800));
            }
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setAdjustViewBounds(true);
        } else {
            imageView = (ImageView) convertView;
        }
        Picasso.with(mContext).load(arrayList.get(position)).into(imageView);

        return imageView;
    }


}