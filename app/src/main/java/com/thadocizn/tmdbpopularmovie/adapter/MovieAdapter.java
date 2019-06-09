package com.thadocizn.tmdbpopularmovie.adapter;

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
import com.thadocizn.tmdbpopularmovie.R;
import com.thadocizn.tmdbpopularmovie.model.Movie;
import com.thadocizn.tmdbpopularmovie.view.MovieDetailActivity;

import java.util.ArrayList;

/**
 * Created by charles on 08,June,2019
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Movie> movieArrayList;

    public MovieAdapter(Context context, ArrayList<Movie> movieArrayList) {
        this.context = context;
        this.movieArrayList = movieArrayList;
    }

    @NonNull
    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.MovieViewHolder movieViewHolder, int position) {

        movieViewHolder.movieTitle.setText(movieArrayList.get(position).getOriginalTitle());
        movieViewHolder.rate.setText(Double.toString(movieArrayList.get(position).getVoteAverage()));

        String image = "https://image.tmdb.org/t/p/w500"+movieArrayList.get(position).getPosterPath();

        Glide.with(context)
                .load(image)
                .placeholder(R.drawable.loading)
                .into(movieViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return movieArrayList.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {

       private TextView movieTitle, rate;
       private ImageView imageView;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.ivMovie);
            movieTitle = itemView.findViewById(R.id.tvTitle);
            rate = itemView.findViewById(R.id.tvRating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION){
                        Movie selectedMovie = movieArrayList.get(pos);

                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra("movie", selectedMovie);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                        String movie = selectedMovie.getOriginalTitle();
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
