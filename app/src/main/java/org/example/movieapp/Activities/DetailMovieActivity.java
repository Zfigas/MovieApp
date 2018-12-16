package org.example.movieapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.example.movieapp.Database.AppDatabase;
import org.example.movieapp.Fragments.SearchFragment;
import org.example.movieapp.Model.Movie;
import org.example.movieapp.R;

import java.util.ArrayList;
import java.util.List;


public class DetailMovieActivity extends AppCompatActivity
{

    final String imageBig = "https://image.tmdb.org/t/p/w500";
    boolean isInDb;
    Movie movie;

    private AppDatabase database;

    ImageView bannerImage;
    Button button;
    List<Movie> movieList;
    TextView movieTitle,movieAvgRate,movieReleaseDate,movieDescription, moviePopularity, your_rating;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_movie);
        Toolbar toolbar =findViewById(R.id.toolbar);
        //remove title of menu
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //to add back button from detail activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        isInDb = false;
        Intent intent = getIntent();

        bannerImage = findViewById(R.id.poster_image_detailed);
        movieTitle =  findViewById(R.id.title_detailed);
        movieAvgRate = findViewById(R.id.rating_detailed);
        movieReleaseDate = findViewById(R.id.release_date_detailed);
        movieDescription = findViewById(R.id.description_detailed);
        moviePopularity = findViewById(R.id.popularity_detailed);
        your_rating = findViewById(R.id.your_rating);
        button = findViewById(R.id.rateMovie);

        movie = new Movie(intent.getStringExtra("imageName"), intent.getStringExtra("movieName"), intent.getStringExtra("description"),
                intent.getStringExtra("userRating"), intent.getStringExtra("releaseDate"), intent.getStringExtra("popularity"),
                intent.getStringExtra("yourRating"), intent.getStringExtra("isFavourite"));

        movieTitle.setText("Title: " + intent.getStringExtra("movieName"));
        movieAvgRate.setText("Rating: " + intent.getStringExtra("userRating"));
        movieDescription.setText("Description: " +intent.getStringExtra("description"));
        movieReleaseDate.setText("Released: " + intent.getStringExtra("releaseDate"));
        moviePopularity.setText("Popularity: " +intent.getStringExtra("popularity"));
        String imagePath = intent.getStringExtra("imageName");
        movieList = new ArrayList<>();
        database = AppDatabase.getDatabase(getApplicationContext());
        movieList = database.movieDao().getAllMovie();


        /*
        checks movielist, if movie is in database set movie rating and movie isfavourite values to data from intent
         */
        if (movieList!=null) {
            for (Movie temp : movieList) {
                if (temp.movieName.equals(movie.movieName)) {
                    isInDb = true;
                    movie.yourRating = temp.yourRating;
                    movie.isFavourite = temp.isFavourite;
                }

            }
        }
        // if rating = -1 do not display it
        if (movie.yourRating.equals("-1")){
            your_rating.setVisibility(View.INVISIBLE);

        }
        else{
            your_rating.setText("Your rating: " + movie.yourRating);
        }

        // button to add alert dialog with rating possibility
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog;
                alertDialog = new AlertDialog.Builder(DetailMovieActivity.this).create();
                ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                        DetailMovieActivity.this, R.array.rate_array, android.R.layout.simple_spinner_item);
                View mView = getLayoutInflater().inflate(R.layout.dialog_spinner, null);
                final Spinner spinner = mView.findViewById(R.id.dialogSpinner);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter2);
                alertDialog.setTitle("Rating");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Rate movie", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        movie.yourRating = spinner.getSelectedItem().toString();
                        isInDb = true;
                        database.movieDao().removeMovie(movie.movieName);
                        database.movieDao().addMovie(movie);
                        Toast toast;
                        toast = Toast.makeText(getApplicationContext(), movie.movieName + " was rated!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        your_rating.setVisibility(View.VISIBLE);
                        your_rating.setText("Your rating: " + movie.yourRating);


                    }
                });
                if (!movie.yourRating.equals("-1")){
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Remove rating", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        movie.yourRating = "-1";
                        database.movieDao().updateMovie(movie.yourRating, movie.movieName);
                        your_rating.setVisibility(View.INVISIBLE);
                        your_rating.setText("Your rating: " + movie.yourRating);
                        //...

                    }
                });
            }
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }});

                alertDialog.setView(mView);
                alertDialog.show();
            }
        });

        if (imagePath.equals("N/A")){
            Picasso.get().load(R.drawable.image_not_found).into(bannerImage);
        } else {
         Picasso.get().load(imageBig + imagePath).into(bannerImage);
       }
       }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    /*
    Override to change heart shaped icon to full red heart if movie is in favourites
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        database = AppDatabase.getDatabase(getApplicationContext());
        movieList = database.movieDao().getAllMovie();

        if (movieList!=null){
        for (Movie temp : movieList) {
            if (temp.movieName.equals(movie.movieName) && temp.isFavourite.equalsIgnoreCase("true")) {
                isInDb = true;
                movie.yourRating = temp.yourRating;
                MenuItem settingsItem = menu.findItem(R.id.action_addToFavourite);
                // set your desired icon here based on a flag if you like
                settingsItem.setIcon(getResources().getDrawable(R.drawable.full_heart));
            }
        }
        }


        return super.onPrepareOptionsMenu(menu);
    }

    /*
    Override to add to favourites or to remove from favourites
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast toast;

        if (item.getItemId() == R.id.action_addToFavourite) {
            if (movie.isFavourite.equalsIgnoreCase("true") && !movie.yourRating.equals("-1")) {
                movie.isFavourite="false";
                database.movieDao().removeMovie(movie.movieName);
                database.movieDao().addMovie(movie);
                toast = Toast.makeText(getApplicationContext(), movie.movieName + " has been removed from favourites, stays in rated", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                item.setIcon(R.drawable.empty_heart);
                isInDb = false;
            }
            else if (movie.isFavourite.equalsIgnoreCase("true")) {
                movie.isFavourite="false";
                database.movieDao().removeMovie(movie.movieName);
                toast = Toast.makeText(getApplicationContext(), movie.movieName + " has been removed from favourites", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                item.setIcon(R.drawable.empty_heart);
                isInDb = false;
            }
           else {
                movie.isFavourite="true";
                database.movieDao().removeMovie(movie.movieName);
                database.movieDao().addMovie(movie);
                toast = Toast.makeText(getApplicationContext(), movie.movieName + " added to favourites", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                item.setIcon(R.drawable.full_heart);
                isInDb = true;

            }
        }
        return super.onOptionsItemSelected(item);

    }


}
