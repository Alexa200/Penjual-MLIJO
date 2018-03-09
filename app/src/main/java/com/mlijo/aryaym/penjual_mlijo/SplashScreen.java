package com.mlijo.aryaym.penjual_mlijo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mlijo.aryaym.penjual_mlijo.Autentifikasi.AutentifikasiTeleponActivity;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by AryaYM on 12/08/2017.
 */

public class SplashScreen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


                auth = FirebaseAuth.getInstance();
                if (auth.getCurrentUser()!=null){
                    startActivity(new Intent(SplashScreen.this,MainActivity.class));
                }else {
                    startActivity(new Intent(SplashScreen.this,AutentifikasiTeleponActivity.class));
                }
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
