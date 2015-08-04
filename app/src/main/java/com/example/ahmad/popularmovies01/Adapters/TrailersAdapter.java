package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ahmad.popularmovies01.Objects.Trailer;
import com.example.ahmad.popularmovies01.R;

import java.util.List;

/**
 * Created by ahmad on 22/06/2015.
 */

public class TrailersAdapter extends ArrayAdapter<Trailer> {
    private final Context context;
    private final List<Trailer> values;

    public TrailersAdapter(Context context, List<Trailer> values) {
        super(context, R.layout.trailers, values);
        this.context = context;
        this.values = values;
    }

    public int getCount() {
        return values.size();
    }

    // here i am not using convertView as i want to show all the list items with out scrolling so
    // I need to create all rows
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.trailers, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.trailers_list_image_view);
        imageView.setImageResource(R.drawable.youtube);
        TextView textView = (TextView) rowView.findViewById(R.id.trailers_list_text);
        textView.setText(values.get(position).getName());
        return rowView;
    }
 /*   @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // A holder will hold the references
        // to your views.
        ViewHodler holder;

        if (convertView == null) {
            View rowView = inflater.inflate(R.layout.trailers, parent, false);
            holder = new ViewHodler();
            holder.someTextView = (TextView) rowView.findViewById(R.id.trailers_list_text);
            holder.someImageView = (ImageView) rowView.findViewById(R.id.trailers_list_image_view);
            rowView.setTag(holder);
        } else {
            holder = (ViewHodler) convertView.getTag();
        }
        holder.someTextView.setText("Watch Trailer" + position);
        if (values.get(position) != null)
            Picasso.with(context)
                    .load(values.get(position))
                    .into(holder.someImageView);

        return convertView;
    }

    class ViewHodler {
        // declare your views here
        TextView someTextView;
        ImageView someImageView;
    }*/
}
