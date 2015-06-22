package com.example.ahmad.popularmovies01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahmad on 22/06/2015.
 */

public class TrailersAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public TrailersAdapter(Context context, ArrayList<String> values) {
        super(context, R.layout.trailers, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.trailers, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.trailers_list_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.trailers_list_image_view);
        imageView.setImageResource(R.drawable.youtube);
        int newPos = position+1;
        textView.setText("Watch Trailer ");
        return rowView;
    }
}
