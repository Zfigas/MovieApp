package org.example.movieapp.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.example.movieapp.Activities.DetailMovieActivity;
import org.example.movieapp.Adapters.MoviesAdapter;
import org.example.movieapp.CheckInternet.CheckNetwork;
import org.example.movieapp.HttpHandlers.HttpHandler;
import org.example.movieapp.Model.Movie;
import org.example.movieapp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MovieFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<Movie> movieList;
    MoviesAdapter adapter;
    ListView listView;
    Boolean loading_data;
    Spinner spinner;
    String chosenFromSpinner = "Discover",url;
    int pageCount = 1;

    final String api_popular = "https://api.themoviedb.org/3/movie/popular?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&page=";
    final String api_newestMovies = "https://api.themoviedb.org/3/movie/upcoming?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&page=";
    final String api_oldestMovies ="https://api.themoviedb.org/3/discover/movie?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&sort_by=primary_release_date.asc&include_adult=false&include_video=false&page=";
    final String api_highestRate = "https://api.themoviedb.org/3/movie/top_rated?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&page=";
    final String api_highestVote = "https://api.themoviedb.org/3/discover/movie?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&sort_by=vote_count.desc&include_adult=false&include_video=false&page=";
    final String api_trend = "https://api.themoviedb.org/3/trending/all/week?api_key=98ba0b1b619d28baf631a4f13b29f816&page=";
    final String api_nowPlaying = "https://api.themoviedb.org/3/movie/now_playing?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&page=";


    public MovieFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //Fragment will not be destroyed on configuration changes.
        setRetainInstance(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_list, container, false);
        listView = v.findViewById(R.id.list);

        movieList = new ArrayList<>();
        loading_data = false;
        spinner = v.findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.spinner_array, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            /*
            Start new async task to get relevant data based on item taken from spinner
             */

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chosenFromSpinner = spinner.getSelectedItem().toString();
                pageCount = 1;
                movieList.clear();
                if(CheckNetwork.isInternetAvailable(getContext())) {
                    new getMovies().execute();
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        listView.setOnItemClickListener(this);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //do nothing
            }
            /*
            On scroll checks if you see last item in list view, if there is internet connection,
            if yes then start new asyncTask based on value of page, and spinner,
            if no then make sure list view cant see last item
             */

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(getContext() !=null){
                    if (CheckNetwork.isInternetAvailable(getContext())) {
                        if (!loading_data) {
                            loading_data = true;
                            pageCount++;
                            new getMovies().execute();
                            adapter.notifyDataSetChanged();
                        }
                    }
                        }
                    else{
                        Toast.makeText(getContext(),"No internet connection", Toast.LENGTH_LONG).show();
                        listView.smoothScrollToPosition(0);
                    }

                }
            }
        });
        return v;
    }

    /*
    On item click sends to detailMovieActivity where you can see details about movie
     */
            public void onItemClick(AdapterView parent, View v, int position, long id){
        Movie movie = adapter.getItem(position);
        if (movie!=null) {
            goToDetailedPage(movie.imagePath, movie.userRating, movie.movieName, movie.description, movie.releaseDate, movie.popularity, movie.yourRating, movie.isFavourite);
        }
    }

    /*
    Prepares intent about chosen movie and start detailed movie activity
     */

    public void goToDetailedPage(String imagePath, String userRating, String movieName,
                                        String description, String releaseDate, String popularity, String yourRating, String isFavourite) {
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
    Method which handles data after postExecute in async task - getMovies
     */

    public void putDataToArrayList(){

            if (getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (pageCount == 1) {
                            if (movieList.size() == 0 && CheckNetwork.isInternetAvailable(getContext())) {
                                pageCount = 1;
                                new getMovies().execute();
                            } else {
                                adapter = new MoviesAdapter(getContext(), movieList);
                                listView.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }
                        } else {
                            loading_data = false;
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            }
        }




    @Override
    public void onResume() {
        super.onResume();
    }

    /*
    getMovies is asyncTask which downloads data in background
     */

     class getMovies extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            if (spinner.getSelectedItem().toString().equals("Discover")) {
                url = api_popular + pageCount;

            } else if (spinner.getSelectedItem().toString().equals("Trending")) {
                url = api_trend + pageCount;
            }
            else if (spinner.getSelectedItem().toString().equals("Most Voted")){
                url = api_highestVote+pageCount;
            }
            else if (spinner.getSelectedItem().toString().equals("Highest rated")){
                url = api_highestRate+pageCount;
            }
            else if (spinner.getSelectedItem().toString().equals("Upcoming")){
                url = api_newestMovies+pageCount;
            }
            else if (spinner.getSelectedItem().toString().equals("Oldest")){
                url = api_oldestMovies+pageCount;
            }
            else if (spinner.getSelectedItem().toString().equals("In cinemas")){
                url = api_nowPlaying+pageCount;
            }
            if (CheckNetwork.isInternetAvailable(getContext())) {
                String jsonStr = sh.makeServiceCall(url);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");

                    // looping through all results
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject c = results.getJSONObject(i);
                        String vote = c.getString("vote_average");
                        String title = c.getString("title");
                        String image = c.getString("poster_path");
                        String description = c.getString("overview");
                        String releaseDate = c.getString("release_date");
                        String popularity = c.getString("popularity");
                        String yourRating = "-1";
                        String isFavourite = "False";

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
                    // do nothing
                }
            }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
                putDataToArrayList();
            }


        }
    }


