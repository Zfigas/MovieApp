package org.example.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.example.movieapp.Model.Movie;
import org.example.movieapp.R;

import java.util.ArrayList;

public class MoviesAdapter extends ArrayAdapter<Movie> {

    final String imageSmall = "https://image.tmdb.org/t/p/w300";

    ImageView bannerImage;
    TextView movieTitle,movieAvgRate;
    public MoviesAdapter(Context context, ArrayList<Movie> movies) {

        super(context, 0, movies);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        // Get the data item for this position
        Movie movie = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.movie_list, parent, false);
        }
        // Lookup view for data population

        bannerImage = view.findViewById(R.id.poster_image);
        movieTitle =  view.findViewById(R.id.title);
        movieAvgRate = view.findViewById(R.id.avg_vote);

        movieTitle.setText("Title: " + movie.movieName);
        movieAvgRate.setText("Rating: " + movie.userRating);

        if(movie.imagePath.equals("N/A")) {
            Picasso.get().load(R.drawable.image_not_found).resize(300, 300).into(bannerImage);
        }
        else{

            Picasso.get().load(imageSmall + movie.imagePath).into(bannerImage);
        }

        // Return the completed view to render on screen
        return view;
    }

}
