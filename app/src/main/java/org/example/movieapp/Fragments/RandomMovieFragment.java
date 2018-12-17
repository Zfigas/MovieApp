package org.example.movieapp.Fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import org.example.movieapp.CheckInternet.CheckNetwork;
import org.example.movieapp.HttpHandlers.HttpHandler;
import org.example.movieapp.Model.Movie;
import org.example.movieapp.R;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class RandomMovieFragment extends Fragment {

    final String api_randomMovie = "https://api.themoviedb.org/3/movie/";
    final String api_restOfString = "?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US";
    final String imageBig = "https://image.tmdb.org/t/p/w500";
    int randomCount;
    Boolean finished = false;
    Movie movie;

    ImageView bannerImage;
    TextView movieTitle,movieAvgRate,movieReleaseDate,movieDescription, moviePopularity;
    Button button;


    public RandomMovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Fragment will not be destroyed on configuration changes.
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        // super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.random_movie,container,false);
        bannerImage = v.findViewById(R.id.poster_image_detailed1);
        movieTitle =  v.findViewById(R.id.title_detailed1);
        movieAvgRate = v.findViewById(R.id.rating_detailed1);
        movieReleaseDate = v.findViewById(R.id.release_date_detailed1);
        movieDescription = v.findViewById(R.id.description_detailed1);
        moviePopularity = v.findViewById(R.id.popularity_detailed1);
        button = v.findViewById(R.id.randomButton);
        button.setOnClickListener(new View.OnClickListener() {
            /*
             Button which checks if internet is available if yes then call async task to get new random movie
             */
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(getContext())) {
                    new getRandomMovie().execute();
                }
                else{
                    Toast.makeText(getContext(),"No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        new getRandomMovie().execute();
        return v;
    }
    /*
    Method to handle data after post execute
     */
    public void loadData(){
        if (movie != null){
            movieTitle.setText("Title: " + movie.movieName);
            movieAvgRate.setText("Rating: " + movie.userRating);
            movieReleaseDate.setText("Released: " +  movie.releaseDate);
            movieDescription.setText("Description: " + movie.description);
            moviePopularity.setText("Popularity: " + movie.popularity);
            if (movie.imagePath.equals("N/A")){
                Picasso.get()
                        .load(R.drawable.image_not_found)
                        .into(bannerImage);
            }
            else{

            Picasso.get()
                    .load(imageBig+movie.imagePath)
                    .into(bannerImage);
            finished = true;
           }
        }
    }

     /*
    Async task class which downloads data in background
     */

    class getRandomMovie extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            Random r = new Random();
            randomCount = (r.nextInt(500000 - 10) + 10);
                // Making a request to url and getting response
                String url = api_randomMovie + randomCount + api_restOfString;
                String jsonStr = sh.makeServiceCall(url);

                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);

                        String vote = jsonObj.getString("vote_average");
                        String title = jsonObj.getString("original_title");
                        String image = jsonObj.getString("poster_path");

                        String description = jsonObj.getString("overview");
                        String releaseDate = jsonObj.getString("release_date");
                        String popularity = jsonObj.getString("popularity");
                        String yourRating = "-1";
                        String isFavourite = "False";

                        if (vote.equals("0.0")) {
                            vote = "N/A";
                        }
                        if (title.equals("")) {
                            title = "N/A";
                        }
                        if (image.equals("null")) {
                            image = "N/A";
                        }
                        if (description.equals("")) {
                            description = "N/A";
                        }
                        if (releaseDate.equals("")) {
                            releaseDate = "N/A";
                        }
                        if (popularity.equals("0.0")) {
                            popularity = "N/A";
                        }
                        movie = new Movie(image, title, description, vote, releaseDate, popularity, yourRating, isFavourite);


                    } catch (final JSONException e) {

                    }

                } else {
                    if (CheckNetwork.isInternetAvailable(getContext())) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new getRandomMovie().execute();
                            }
                        });
                    }

                }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            loadData();
           }
        }
    }




