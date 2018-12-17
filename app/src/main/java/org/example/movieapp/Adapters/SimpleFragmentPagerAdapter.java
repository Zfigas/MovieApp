package org.example.movieapp.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import org.example.movieapp.Fragments.MovieFragment;
import org.example.movieapp.Fragments.YourMoviesFragment;
import org.example.movieapp.Fragments.RandomMovieFragment;
import org.example.movieapp.Fragments.SearchFragment;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

     Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        
        super(fm);
        mContext = context;
    }
    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new MovieFragment();
        }
        else if (position == 1) {
            return new RandomMovieFragment();
        }

        else if (position == 2){
            return new SearchFragment();
        }
        else {
            return new YourMoviesFragment();
        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 4;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Movies";
            case 1:
                return "Random";
            case 2:
               return "Search";
            case 3:
                return "Your movies";
            default:
                return null;
        }
    }

}