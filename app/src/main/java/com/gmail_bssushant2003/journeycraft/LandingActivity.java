package com.gmail_bssushant2003.journeycraft;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gmail_bssushant2003.journeycraft.Login.SendOTPActivity;
import com.gmail_bssushant2003.journeycraft.Questions.WelcomeActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LandingActivity extends AppCompatActivity {

    TextView txtTravel;
    RelativeLayout relativeLayout;
    Animation txtAnimation, layoutAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(getResources().getColor(android.R.color.white));
        setContentView(R.layout.activity_landing);

        //change color of status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(android.R.color.white));
        }



        txtAnimation = AnimationUtils.loadAnimation(LandingActivity.this,R.anim.fall_down);
        layoutAnimation = AnimationUtils.loadAnimation(LandingActivity.this,R.anim.bottom_to_top);

        txtTravel = findViewById(R.id.travel);
        relativeLayout = findViewById(R.id.main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout.setAnimation(layoutAnimation);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        txtTravel.setVisibility(View.VISIBLE);
                        txtTravel.setAnimation(txtAnimation);
                    }
                },800);
            }
        },900);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences recordFile = getSharedPreferences("records", Context.MODE_PRIVATE);
                boolean isUserValid = recordFile.getBoolean("isUserValid", false);

                if(isUserValid){
                    Intent intent = new Intent(LandingActivity.this, DestinationListActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
//                    Intent intent = new Intent(LandingActivity.this, WelcomeActivity.class);
                    Intent intent = new Intent(LandingActivity.this, SendOTPActivity.class);
                    startActivity(intent);
                    finish();
        }
            }
        },4000);

    }
}



