package org.example.movieapp.Fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.example.movieapp.Adapters.MoviesAdapter;
import org.example.movieapp.Activities.DetailMovieActivity;
import org.example.movieapp.CheckInternet.CheckNetwork;
import org.example.movieapp.HttpHandlers.HttpHandler;
import org.example.movieapp.Model.Movie;
import org.example.movieapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class SearchFragment extends Fragment  implements AdapterView.OnItemClickListener  {

    final String api_search = "https://api.themoviedb.org/3/search/movie?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&query=";
    String url;
    String forResult = "";
    boolean loading_data;
    int pageCount = 1;

    MoviesAdapter adapter;
    ArrayList<Movie> movieList;

    EditText editText;
    Button button;
    ListView listView;

    public SearchFragment() {
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
        View v = inflater.inflate(R.layout.search_movie, container, false);
        movieList = new ArrayList<>();
        listView = v.findViewById(R.id.listView1);
        editText = v.findViewById(R.id.editText);
        loading_data = false;
        button = v.findViewById(R.id.searchButton);
        listView.setOnItemClickListener(this);
        button.setOnClickListener(new View.OnClickListener() {
            /*
            Button onClick takes data from editText,checks internet connection, and makes Async search
             */
            @Override
            public void onClick(View v) {

                if (CheckNetwork.isInternetAvailable(getContext())) {
                    movieList.clear();
                    pageCount = 1;
                    forResult = editText.getText().toString();
                    new searchForMovies().execute();
                }
                else{
                    Toast.makeText(getContext(),"No internet connection", Toast.LENGTH_LONG).show();
                }
                // Hides keyboard after clicking search button
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
                if (getActivity().getCurrentFocus().getWindowToken() !=null) {
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
            }
        });
        return v;
    }

    /*
    On click go to detailed page about chosen movie
     */
    public void onItemClick(AdapterView parent, View v, int position, long id){
        Movie movie = adapter.getItem(position);
        goToDetailedPage(movie.imagePath,movie.userRating,movie.movieName,movie.description,movie.releaseDate,movie.popularity, movie.yourRating, movie.isFavourite);
    }
    /*
    Method which takes data about movie and put it to intent, start detailedMovieActivity with this data
     */
    public void goToDetailedPage(String imagePath, String userRating, String movieName,
                                 String description, String releaseDate, String popularity,String yourRating, String isFavourite) {
        Intent intent = new Intent(getActivity(), DetailMovieActivity.class);
        intent.putExtra("imageName", imagePath);
        intent.putExtra("userRating", userRating);
        intent.putExtra("movieName", movieName);
        intent.putExtra("description", description);
        intent.putExtra("releaseDate", releaseDate);
        intent.putExtra("popularity", popularity);
        intent.putExtra("yourRating", yourRating);
        intent.putExtra("isFavourite", isFavourite);
        startActivity(intent);
    }
    /*
    Method to take care of data after post execute
     */
    public void handlePostExecute() {
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (movieList.size() == 0) {
                        Toast.makeText(getActivity(),
                                "Does not exist in TMDB API: ",
                                Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new MoviesAdapter(getActivity().getApplicationContext(), movieList);
                        listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
    }
    }

    /*
    Async task class which downloads data in background
     */

    public class searchForMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            url = api_search+forResult+"&page=" + pageCount + "include_adult=false";


            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");


                    // looping through all results
                    movieList = new ArrayList<>();

                    for (int i = 0; i < results.length(); i++) {

                        JSONObject c = results.getJSONObject(i);
                        String vote = c.getString("vote_average");
                        String title = c.getString("title");
                        String image = c.getString("poster_path");
                        String description = c.getString("overview");
                        String releaseDate = c.getString("release_date");
                        String popularity = c.getString("popularity");
                        String yourRating = "-1";
                        String isFavourite = "false";

                        if (vote.equals("0.0")) {
                            vote = "N/A";
                        }
                        if (vote.equals("0")) {
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
                        Movie movie = new Movie(image, title, description, vote, releaseDate, popularity, yourRating, isFavourite);
                        movieList.add(movie);
                    }
                } catch (final JSONException e) {
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handlePostExecute();
        }

    }
}

