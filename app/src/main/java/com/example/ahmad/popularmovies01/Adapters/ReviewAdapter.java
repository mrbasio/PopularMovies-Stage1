package com.example.ahmad.popularmovies01.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ahmad.popularmovies01.Objects.Review;
import com.example.ahmad.popularmovies01.R;

import java.util.List;

/**
 * Created by ahmad on 06/09/2015.
 */
public class ReviewAdapter extends BaseAdapter {
    Context context;
    List<Review> arrayList;

    public ReviewAdapter(Context c, List<Review> arrayList) {
        this.arrayList = arrayList;
        this.context = c;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        public TextView author;
        public TextView content;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.review_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.author = (TextView) convertView.findViewById(R.id.reviewer_name);
            viewHolder.content = (TextView) convertView.findViewById(R.id.review_content);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.author.setText(arrayList.get(position).getAuthor());
        holder.content.setText(arrayList.get(position).getContent());
        return convertView;
    }
}
