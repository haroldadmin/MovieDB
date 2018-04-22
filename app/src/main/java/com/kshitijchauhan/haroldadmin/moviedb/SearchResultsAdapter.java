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

import java.util.ArrayList;

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
        yearTextView.setText(searchResult.getReleaseDate().substring(0, 4));
        Glide
                .with(getContext())
                .load("https://image.tmdb.org/t/p/w92" + searchResult.getPosterPath())
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
            poster = cardView.findViewById(R.id.media_image);
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
