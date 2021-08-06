package com.example.onlineshopping.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.example.onlineshopping.R;
import com.example.onlineshopping.ui.OnBoardingScreenActivity;
import android.content.Context;

import org.jetbrains.annotations.NotNull;

public class SliderAdapter extends PagerAdapter {
    Context context ;
    LayoutInflater layoutInflater ;
    public SliderAdapter(Context context)
    {
        this.context = context ;
    }
    public int [] slide_images = {
            R.drawable.purchaseorder ,
            R.drawable.creditcard ,
            R.drawable.fooddelivery

    };

    public String [] slide_heading = {
           "Choose your orders",
            "Easy Payment" ,
            "Fast Delivery"

    };
    public String [] slide_desc = {
            "Choose your orders and we will prepare your order immediately",
            "You can easily pay by cash or credit card to finish the purchase",
            "We will send your orders as soon as possible"

    };



    @Override
    public int getCount() {
        return slide_heading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull @NotNull View view, @NonNull @NotNull Object object) {
        return view == object;
    }

    @NonNull
    @NotNull
    @Override
    public Object instantiateItem(@NonNull @NotNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slide , container , false);
        ImageView imageView  = view.findViewById(R.id.slideImage);
        TextView textViewHead = view.findViewById(R.id.slideHead);
        TextView textViewDesc = view.findViewById(R.id.slideDesc);
        imageView.setImageResource(slide_images[position]);
        textViewHead.setText(slide_heading[position]);
        textViewDesc.setText(slide_desc[position]);
        container.addView(view);
        return view ;
    }

    @Override
    public void destroyItem(@NonNull @NotNull ViewGroup container, int position, @NonNull @NotNull Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
