package com.example.instashop;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity
{
    //Delay time variable (3000 = 3 seconds)
    private final int splash_delay = 1000;

    //Widget field
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setBackgroundDrawable(null);

        //methods to call
        hideNavigationBar();
        initImageView();
        animateImage();
        move_to_main_activity();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //hideNavigationBar();
    }

    private void hideNavigationBar()
    {
        this.getWindow().getDecorView()
                .setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                );
    }

    private void initImageView()
    {
        imageView = findViewById(R.id.imageView);
    }

    private void animateImage()
    {
        Animation fade_in = AnimationUtils.loadAnimation( this, R.anim.fade);
        fade_in.setDuration(splash_delay);
        imageView.startAnimation(fade_in);
    }

    private void move_to_main_activity()
    {
        new Handler().postDelayed(()->
                {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }, splash_delay);
    }

}
