package com.example.onlineshopping.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.onlineshopping.R;
import com.example.onlineshopping.adapters.CategoryAdapter;
import com.example.onlineshopping.adapters.MainBannerAdapter;
import com.example.onlineshopping.model.CategoryData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.indicator.enums.IndicatorSlideMode;
import com.zhpan.indicator.enums.IndicatorStyle;

import java.util.ArrayList;
public class HomeActivity extends AppCompatActivity implements CategoryAdapter.ItemOnClickListener {
    Animation rotateOpen , rotateClose , fromBottom , toBottom ;
    MainBannerAdapter mainBannerAdapter ;
    FloatingActionButton cartFabBtn , addFabBtn , searchFabBtn ;
     BannerViewPager<Integer> mViewPager ;
    ArrayList<Integer>mDrawableList ;
    RecyclerView recyclerView ;
    GridLayoutManager gridLayoutManager ;
    ArrayList<CategoryData> arrayList ;
    Boolean clicked ;
    CategoryAdapter mCategoryAdapter ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rotateOpen = AnimationUtils.loadAnimation(this , R.anim.rotate_open);
        rotateClose = AnimationUtils.loadAnimation(this , R.anim.rotate_close);
        fromBottom = AnimationUtils.loadAnimation(this , R.anim.from_bottom);
        toBottom = AnimationUtils.loadAnimation(this , R.anim.to_bottom);
        cartFabBtn = findViewById(R.id.btnFabMyCart);
        addFabBtn = findViewById(R.id.btnFabMenu);
        searchFabBtn = findViewById(R.id.btnFabSearch);
        mViewPager = findViewById(R.id.banner_view) ;
        mainBannerAdapter = new MainBannerAdapter();
        arrayList = new ArrayList<>();
        arrayList=setDataInHome();
        mCategoryAdapter = new CategoryAdapter(this , arrayList,this);
        recyclerView = findViewById(R.id.categoryRecyclerView);
        gridLayoutManager = new GridLayoutManager(this, 3 , LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mCategoryAdapter);
        mDrawableList = new ArrayList<>();
        mDrawableList.add(R.drawable.bancloth);
        mDrawableList.add(R.drawable.banelect);
        mDrawableList.add(R.drawable.banfur);
        setupViewPager();
        clicked = false ;
        addFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddFabClicked();
            }
        });
        cartFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this , MyCartActivity.class);
                startActivity(i);
            }
        });
        searchFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HomeActivity.this , SearchProduct.class);
                startActivity(i);
            }
        });
    }
    private void onAddFabClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }
    private void setVisibility(Boolean clicked) {
        if (!clicked)
        {
            searchFabBtn.setVisibility(View.VISIBLE);
            cartFabBtn.setVisibility(View.VISIBLE);
        }else{
            searchFabBtn.setVisibility(View.INVISIBLE);
            cartFabBtn.setVisibility(View.INVISIBLE);
        }
    }
    private void setAnimation(Boolean clicked) {
        if (!clicked)
        {
            searchFabBtn.startAnimation(fromBottom);
            cartFabBtn.startAnimation(fromBottom);
            addFabBtn.startAnimation(rotateOpen);
        }else{
             searchFabBtn.startAnimation(toBottom);
             cartFabBtn.startAnimation(toBottom);
             addFabBtn.startAnimation(rotateClose);
        }
    }
    private void setupViewPager(){
    mViewPager.setAdapter(mainBannerAdapter);
    mViewPager.setAutoPlay(true);
    mViewPager.setLifecycleRegistry(getLifecycle());
    mViewPager.setIndicatorStyle(IndicatorStyle.ROUND_RECT);
    mViewPager.setIndicatorSlideMode(IndicatorSlideMode.SMOOTH);
    mViewPager.setIndicatorSliderColor(   ContextCompat.getColor(this, R.color.white),
            ContextCompat.getColor(this, R.color.white));
    mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
        }
    }).create(mDrawableList);
}
private ArrayList<CategoryData> setDataInHome(){
    ArrayList<CategoryData>items = new ArrayList<>();
    items.add(new CategoryData(R.drawable.catcloth , "clothes"));
    items.add(new CategoryData(R.drawable.catlap , "laptops"));
    items.add(new CategoryData(R.drawable.catmob , "mobiles"));
    items.add(new CategoryData(R.drawable.catelect , "electronics"));
    items.add(new CategoryData(R.drawable.catfur , "furniture"));
    items.add(new CategoryData(R.drawable.catmak , "makeup"));
    return items ;
}
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(HomeActivity.this , ProductActivity.class);
        i.putExtra("category" , arrayList.get(position).getTitle());
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}