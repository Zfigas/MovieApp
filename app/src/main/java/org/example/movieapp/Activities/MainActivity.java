package org.example.movieapp.Activities;


import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;
import org.example.movieapp.Adapters.SimpleFragmentPagerAdapter;
import org.example.movieapp.CheckInternet.CheckNetwork;
import org.example.movieapp.R;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the  activity_main.xml layout file
        setContentView(R.layout.activity_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        //In case internet works setup everything required
        if(!CheckNetwork.isInternetAvailable(getApplicationContext())) //returns true if internet available
        {
            //In case of no internet display to user that app will close and close it.
            new CountDownTimer(5000, 1000) {
                int tick = 4;
                Toast toast;

                public void onTick(long millisUntilFinished) {
                    if (tick == 4) {
                        toast = Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT);
                    } else {
                        toast.cancel();
                        toast = Toast.makeText(getApplicationContext(), "Application will close in " + Integer.toString(tick) + " sec", Toast.LENGTH_SHORT);
                    }
                    tick--;
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                public void onFinish() {
                    finish();
                    System.exit(0);
                }
            }.start();
        }
    }
}


