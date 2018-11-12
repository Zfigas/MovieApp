package org.example.movieexamapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ArrayList<Movie> movieList;
    MoviesAdapter adapter;
    ListView listView;
    final String api_discover = "https://api.themoviedb.org/3/discover/movie?api_key=98ba0b1b619d28baf631a4f13b29f816&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1";
   // final String imageBig = "https://image.tmdb.org/t/p/w500";final String imageSmall = "https://image.tmdb.org/t/p/w300";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        movieList = new ArrayList<>();
        listView = findViewById(R.id.list);
        new getMovies().execute();
        listView.setOnItemClickListener(this);

    }

    /*
    On item click on list view. Get position and calls goToDetailedPage which basically prepares intent with data and sends to DetailedMovieActivity
     */
    public void onItemClick(AdapterView parent, View v, int position, long id){
      // adapter.getItem(position);
        //Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();
        Movie movie = adapter.getItem(position);
        goToDetailedPage(movie.imagePath,movie.userRating,movie.movieName,movie.description,movie.releaseDate,movie.popularity);


    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                ArrayList<Movie> searchArray = new ArrayList<>();
//                for (Movie m : movieList) {
//                    if (m.getMovieName().contains("Title: " + query)) {
//                        searchArray.add(m);
//                    }
//                }
//                    if(searchArray.size()>0) {
//
//
//
//                    }
//                    else{
//
//                }
//
//                        return true;
//
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return true;
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
    /*
    Prepares intent to DetailedMovieActivity and starts it.
     */

    public void goToDetailedPage(String imagePath, String userRating, String movieName,
                                 String description, String releaseDate, String popularity) {
        Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
        intent.putExtra("imageName", imagePath);
        intent.putExtra("userRating", userRating);
        intent.putExtra("movieName", movieName);
        intent.putExtra("description", description);
        intent.putExtra("releaseDate", releaseDate);
        intent.putExtra("popularity", popularity);
        startActivity(intent);
    }

/*
getMovies is asyncTask which reads json data, reads it, saves in movie arraylist which later on it uses this data on on main listview.
 */
    class getMovies extends AsyncTask<Void, Void, Void>  {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Json Data is downloading", Toast.LENGTH_SHORT).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = api_discover;
            String jsonStr = sh.makeServiceCall(url);


            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray results = jsonObj.getJSONArray("results");


                    // looping through all results
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject c = results.getJSONObject(i);
                        String vote = "Rating: " + c.getString("vote_average");
                        String title = "Title: " + c.getString("title");
                        String image = c.getString("poster_path");
                        String imageURL =  image;
                        String description = "Description: " + c.getString("overview");
                        String releaseDate = "Release date: " +  c.getString("release_date");
                        String popularity = "Popularity: " + c.getString("popularity");
                        Movie movie = new Movie(imageURL, title, description, vote, releaseDate, popularity);

                        movieList.add(movie);

                    }

                } catch (final JSONException e) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server.",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            adapter = new MoviesAdapter(getApplicationContext(), movieList);
            // Attach the adapter to a ListView
            listView = findViewById(R.id.list);
            listView.setAdapter(adapter);


    }

}
    }