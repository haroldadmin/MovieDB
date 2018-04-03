package com.kshitijchauhan.haroldadmin.moviedb;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class SearchResultsAdapter extends ArrayAdapter<Movie> {

    public static final String LOG_TAG = SearchResultsAdapter.class.getName();

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
        if (searchResult.getPosterURL() != null) {
                try {
                    Bitmap poster = new ImageLoadTask().execute(searchResult.getPosterURL()).get();
                    imageView.setImageBitmap(poster);
                }
                catch (Exception e) {
                    Log.e(LOG_TAG, "Error getting poster for " + searchResult.getName());
                }

            }

        titleView.setText(searchResult.getName());
        yearView.setText(searchResult.getYear());

        return listItemView;
    }
    private class ImageLoadTask extends AsyncTask<URL, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap myBitmap = null;
            URL url = urls[0];
            try {
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);
                Log.e("Bitmap", "returned");
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("Exception", e.getMessage());
            }
            return myBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }
}
