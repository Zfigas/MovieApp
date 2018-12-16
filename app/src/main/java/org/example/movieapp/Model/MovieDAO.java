package org.example.movieapp.Model;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
    public interface MovieDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void addMovie(Movie movie);

        @Query("select * from movie")
        List<Movie> getAllMovie();

        @Query("select * from movie where movieName = :movieName")
        List<Movie> getMovie(String movieName);

        @Query("delete from movie where movieName = :movieName")
        void removeMovie(String movieName);

        @Query("UPDATE Movie SET yourRating = :your_rating WHERE movieName = :movieName")
        void updateMovie(String your_rating, String movieName);

        @Query("UPDATE Movie SET isFavourite = :isFavourite WHERE movieName = :movieName")
        void updateIsFavouriteMovie(String isFavourite, String movieName);

        @Query("delete from movie")
         void removeAllMovie();

    }

