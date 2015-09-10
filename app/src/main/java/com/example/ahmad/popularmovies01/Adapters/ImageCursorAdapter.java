package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.ahmad.popularmovies01.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmad on 03/09/2015.
 */
public class ImageCursorAdapter extends CursorAdapter {

    public ImageCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = new ImageView(context);
        bindView(v, context, cursor);

        int cellSize = parent.getWidth() / 500;

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // if it's not recycled, initialize some attributes
        ImageView imageView = (ImageView) view;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String type = prefs.getString("sort", context.getString(R.string.popularity));
        if (!type.equals("vote_average")) {
            //  if (true){
            imageView.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,
                    GridView.LayoutParams.WRAP_CONTENT));
        } else {
            //I am only doing this because the images coming when asking for most voted movies
            //are not in the same size so the GridViews overlap each other
            LinearLayout.LayoutParams layoutParams = new LinearLayout.
                    LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            imageView.setLayoutParams(layoutParams);
            //imageView.setLayoutParams(new ActionBar.LayoutParams(400, 400));
        }
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setAdjustViewBounds(true);

        if (type.equals("favourite")) {
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                    + "/storage/emulated/0" + "/popularMovies" + "/" + cursor.getString(1)
                    + ".jpg");
            imageView.setImageBitmap(bitmap);
        } else {
            Picasso.with(context).load(cursor.getString(4)).into(imageView);
        }
    }


}
