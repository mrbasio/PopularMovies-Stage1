package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.example.ahmad.popularmovies01.R;
import com.squareup.picasso.Picasso;

/**
 * Created by ahmad on 03/09/2015.
 */
public class ImageCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    public ImageCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return cursorInflater.inflate(R.layout.movie_tem, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) view.findViewById(R.id.movie_poster);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String type = prefs.getString("sort", context.getString(R.string.popularity));
        if (type.equals("favourite")) {
            Bitmap bitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()
                    + "/storage/emulated/0" + "/popularMovies" + "/" + cursor.getString(1)
                    + ".jpg");
            imageView.setImageBitmap(bitmap);
        } else {
            String s = cursor.getString(4);
            Picasso.with(context).load(cursor.getString(4)).into(imageView);
        }
    }


}
