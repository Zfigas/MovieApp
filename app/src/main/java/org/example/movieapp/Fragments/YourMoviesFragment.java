package org.example.movieapp.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import org.example.movieapp.Activities.DetailMovieActivity;
import org.example.movieapp.Adapters.MoviesAdapter;
import org.example.movieapp.Database.AppDatabase;
import org.example.movieapp.Model.Movie;
import org.example.movieapp.Model.MovieDAO;
import org.example.movieapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class YourMoviesFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView listView;
    AppDatabase database;
    MoviesAdapter adapter;
    Spinner spinner;
    String chosenFromSpinner;
    ArrayList<Movie> movieArrayList, movieArrayList2;


    public YourMoviesFragment() {
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
        View v = inflater.inflate(R.layout.main_list,container,false);
        listView = v.findViewById(R.id.list);
        database = AppDatabase.getDatabase(getActivity().getApplicationContext());
        List<Movie> movieList = database.movieDao().getAllMovie();
        movieArrayList = new ArrayList<>();

        //popup whole list
        for (Movie temp : movieList) {
            movieArrayList.add(temp);
        }
        //Copy of list
        movieArrayList2 = new ArrayList<>();
        movieArrayList2.addAll(movieArrayList);

        //Set data to adapter
        adapter = new MoviesAdapter(getActivity(), movieArrayList);
        listView.setAdapter(adapter);
        //Add on click listener
        listView.setOnItemClickListener(this);
        //Refresh list view
        adapter.notifyDataSetChanged();
        spinner = v.findViewById(R.id.spinner1);
        //make movieArrayList2 full of all movies which are favourite
        for (Movie x : movieArrayList2){
            if (x.isFavourite.equalsIgnoreCase("true")){
                movieList.add(x);
            }
        }


        //Setup spinner
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(
                getActivity(), R.array.spinner_array2, android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Based on spinner value get favourite list or rated list
                List<Movie> movieList2 = database.movieDao().getAllMovie();
                chosenFromSpinner = spinner.getSelectedItem().toString();
                ArrayList<Movie> movieList = new ArrayList<>();
                if(chosenFromSpinner.equals("Favourites")){
                    for (Movie x : movieList2){
                        if (x.isFavourite.equalsIgnoreCase("true")){
                            movieList.add(x);
                        }
                    }
                }
                if (chosenFromSpinner.equals("Rated")) {
                    for (Movie x : movieList2){
                        if (!x.yourRating.equals("-1")){
                            movieList.add(x);
                        }
                    }

                }
                adapter.clear();
                adapter.addAll(movieList);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Do nothing
            }
        });

        return v;

    }
    /*
    On click go to detailed page about chosen movie
     */
    public void onItemClick(AdapterView parent, View v, int position, long id) {

        Movie movie = adapter.getItem(position);
        if (movie != null) {
            goToDetailedPage(movie.imagePath, movie.userRating, movie.movieName, movie.description, movie.releaseDate, movie.popularity, movie.yourRating, movie.isFavourite);
        }
    }
    /*
    Method which takes data about movie and put it to intent, start detailedMovieActivity with this data
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
        On resume returns good list from spinner
         */
    @Override
    public void onResume() {
        super.onResume();
        List<Movie> list = database.movieDao().getAllMovie();
        ArrayList<Movie> movieList = new ArrayList<>();
        if(spinner.getSelectedItem().toString().equals("Favourites")){
            for (Movie x : list){
                if (x.isFavourite.equalsIgnoreCase("true")){
                    movieList.add(x);
                }
            }
        }
        if (spinner.getSelectedItem().toString().equals("Rated")) {
            for (Movie x : list){
                if (!x.yourRating.equals("-1")){
                    movieList.add(x);
                }
            }
        }
        adapter.clear();
        adapter.addAll(movieList);
        adapter.notifyDataSetChanged();
    }
}
