package org.example.movieexamapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    final String imageSmall = "https://image.tmdb.org/t/p/w300";

    ImageView bannerImage;
    TextView movieTitle,movieAvgRate;
    public MoviesAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_list, parent, false);
        }
        // Lookup view for data population

        bannerImage = convertView.findViewById(R.id.poster_image);
        movieTitle =  convertView.findViewById(R.id.title);
        movieAvgRate = convertView.findViewById(R.id.avg_vote);

        movieTitle.setText(movie.movieName);
        movieAvgRate.setText(movie.userRating);
        Picasso.get().load(imageSmall+movie.imagePath).into(bannerImage);

        // Return the completed view to render on screen
        return convertView;
    }
}
