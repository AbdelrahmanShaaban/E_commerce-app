package com.example.onlineshopping.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.onlineshopping.R;
import com.example.onlineshopping.adapters.SliderAdapter;

public class OnBoardingScreenActivity extends AppCompatActivity {

    ViewPager mSliderViewPager ;
    LinearLayout mDotsLayout ;
    SliderAdapter sliderAdapter ;
    TextView []mDots ;
    Button nxtBtn , backBtn ;
    int currentPage ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_screen);
        mSliderViewPager = findViewById(R.id.viewPagerSlide);
        mDotsLayout = findViewById(R.id.dots);
        nxtBtn = findViewById(R.id.nextBtn);
        backBtn = findViewById(R.id.prevBtn);
        sliderAdapter = new SliderAdapter(this);
        mSliderViewPager.setAdapter(sliderAdapter);
        addDotsIndicator(0);
        mSliderViewPager.addOnPageChangeListener(onPageChangeListener);
        nxtBtn.setOnClickListener(v -> mSliderViewPager.setCurrentItem(currentPage + 1));
        backBtn.setOnClickListener(v -> mSliderViewPager.setCurrentItem(currentPage - 1));
    }
    public void addDotsIndicator(int position){
        mDots = new TextView[3];
        mDotsLayout.removeAllViews();
        for (int i =0 ; i<mDots.length ; i++)
        {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.yellowWhite));
            mDotsLayout.addView(mDots[i]);
        }
        if (mDots.length>0){
            mDots[position].setTextColor(getResources().getColor(R.color.white));
        }

        if (restorePrefData()){
            Intent i = new Intent(OnBoardingScreenActivity.this , MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    private boolean restorePrefData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref" , MODE_PRIVATE);
        return preferences.getBoolean("isIntroOpened" , false);

    }

    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position ;
            if (position == 0){
                nxtBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);
            } else if (position == mDots.length-1){
                nxtBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nxtBtn.setText("Get Started");
                nxtBtn.setOnClickListener(v -> {
                    Intent i = new Intent(OnBoardingScreenActivity.this , MainActivity.class);
                    startActivity(i);
                    savePrefData();
                    finish();
                });

            }else{
                nxtBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void savePrefData() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("myPref" , MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isIntroOpened" , true);
        editor.commit();
    }
}