package com.kshitijchauhan.haroldadmin.moviedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsAdapter extends ArrayAdapter<Movie> {

    public SearchResultsAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.search_results_list_item, parent, false);
        }

        Movie searchResult = getItem(position);
        TextView titleView = listItemView.findViewById(R.id.movie_name);
        TextView yearView = listItemView.findViewById(R.id.year);
        ImageView imageView = listItemView.findViewById(R.id.poster);
        Bitmap poster = QueryUtils.getBitmapFromURL(searchResult.getPosterURL());
        imageView.setImageBitmap(poster);
        titleView.setText(searchResult.getName());
        yearView.setText(searchResult.getYear());

        return super.getView(position, convertView, parent);
    }
}
