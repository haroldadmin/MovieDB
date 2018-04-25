package com.kshitijchauhan.haroldadmin.moviedb;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private ArrayList<Movie> mSearchResults;
    private Context mContext;

    public SearchResultsAdapter(Context context, ArrayList<Movie> searchResults) {
        mContext = context;
        mSearchResults = searchResults;
    }

    public Context getContext() {
        return mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View searchResultCard = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_card, parent, false);
        ViewHolder holder = new ViewHolder(searchResultCard);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultsAdapter.ViewHolder holder, int position) {
        Movie searchResult = mSearchResults.get(position);
        TextView nameTextView = holder.name;
        TextView yearTextView = holder.year;
        ImageView posterView = holder.poster;
        nameTextView.setText(searchResult.getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        try {
            Date date = dateFormat.parse(searchResult.getReleaseDate());
            yearTextView.setText(dateFormat.format(date));
        }
        catch (ParseException e) {
            yearTextView.setText("Release date N/A");
        }

        Glide
                .with(getContext())
                .load(QueryUtils.getSearchPosterURL(searchResult.getPosterPath()))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.no_image)
                        .error(R.drawable.no_image)
                )
                .into(posterView);
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView year;
        public ImageView poster;

        public ViewHolder (View cardView) {
            super(cardView);
            name = cardView.findViewById(R.id.supporting_text);
            year = cardView.findViewById(R.id.supporting_secondary_text);
            poster = cardView.findViewById(R.id.poster_imageview);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(mContext, MovieDetails.class);
                        intent.putExtra("ID", mSearchResults.get(position).getId());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
