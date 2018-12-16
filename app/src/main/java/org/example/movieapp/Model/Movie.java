package org.example.movieapp.Model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
@Entity
public class Movie implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    long id;
    public String imagePath;
    public String movieName;
    public String description;
    public String userRating;
    public String releaseDate;
    public String popularity;
    public String yourRating;
    public String isFavourite;


    public Movie(String imagePath, String movieName, String description,
                 String userRating, String releaseDate, String popularity, String yourRating, String isFavourite) {
        this.imagePath = imagePath;
        this.movieName = movieName;
        this.description = description;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.popularity = popularity;
        this.yourRating = yourRating;
        this.isFavourite = isFavourite;

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

    public String getYourRating() {return yourRating;}

    public void setYourRating(String yourRating) {this.yourRating = yourRating;}

    public String  getIsFavourite() {return isFavourite;}

    public void setIsFavourite(String isFavourite) {this.isFavourite = isFavourite;}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.imagePath,
                this.movieName,
                this.description,this.userRating,this.releaseDate,this.popularity, this.yourRating, this.isFavourite});
        }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
    public Movie (Parcel pc){
        imagePath = pc.readString();
        movieName = pc.readString();
        description = pc.readString();
        userRating = pc.readString();
        releaseDate = pc.readString();
        popularity = pc.readString();
        yourRating = pc.readString();
        isFavourite = pc.readString();
    }
}


