package com.example.onlineshopping.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineshopping.R;

public class StartActivity extends AppCompatActivity {

    Animation topAnimation , bottomAnimation ;
    ImageView imageView ;
    TextView logoTv , stayTv ;
    private static int SPLASH_SCREEN = 5000 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        topAnimation = AnimationUtils.loadAnimation(this , R.anim.top_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this , R.anim.bottom_animation);
        imageView = (ImageView)findViewById(R.id.imageView2);
        logoTv = (TextView)findViewById(R.id.textView6);
        stayTv = (TextView)findViewById(R.id.textView5);

        imageView.setAnimation(topAnimation);
        logoTv.setAnimation(bottomAnimation);
        stayTv.setAnimation(bottomAnimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent  = new Intent(StartActivity.this , OnBoardingScreenActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_SCREEN);




    }
}