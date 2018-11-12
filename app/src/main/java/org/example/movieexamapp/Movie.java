package org.example.movieexamapp;


public class Movie {

    String imagePath;
    String movieName;
    String description;
    String userRating;
    String releaseDate;
    String popularity;

    public Movie(String imagePath, String movieName, String description,
                              String userRating, String releaseDate, String popularity) {
        this.imagePath = imagePath;
        this.movieName = movieName;
        this.description = description;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

}


