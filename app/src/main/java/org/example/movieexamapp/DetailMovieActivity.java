package org.example.movieexamapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;



public class DetailMovieActivity extends AppCompatActivity {

    final String imageBig = "https://image.tmdb.org/t/p/w500";
    ImageView bannerImage;
    TextView movieTitle,movieAvgRate,movieReleaseDate,movieDescription, moviePopularity;

    /*
    Get data from intent(main activity on click on certain movie get details), set it to correct places and display
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_movie);

        Intent intent = getIntent();

        bannerImage = findViewById(R.id.poster_image_detailed);
        movieTitle =  findViewById(R.id.title_detailed);
        movieAvgRate = findViewById(R.id.rating_detailed);
        movieReleaseDate = findViewById(R.id.release_date_detailed);
        movieDescription = findViewById(R.id.description_detailed);
        moviePopularity = findViewById(R.id.popularity_detailed);


        movieTitle.setText(intent.getStringExtra("movieName"));
        movieAvgRate.setText(intent.getStringExtra("userRating"));
        movieDescription.setText(intent.getStringExtra("description"));
        movieReleaseDate.setText(intent.getStringExtra("releaseDate"));
        moviePopularity.setText(intent.getStringExtra("popularity"));
        String imagePath = intent.getStringExtra("imageName");

        Picasso.get()
                .load(imageBig+imagePath)
                .into(bannerImage);

    }

}
