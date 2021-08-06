package com.example.onlineshopping.adapters;

import android.view.View;
import android.widget.ImageView;


import com.example.onlineshopping.R;
import com.zhpan.bannerview.BaseBannerAdapter;
import com.zhpan.bannerview.BaseViewHolder;

public class MainBannerAdapter extends BaseBannerAdapter<Integer> {


    @Override
    protected void bindData(BaseViewHolder<Integer> holder, Integer data, int position, int pageSize) {
        holder.bindData(data , position , pageSize);
        ImageView bannerImageView = holder.findViewById(R.id.banner_imageView);
        bannerImageView.setImageResource(data);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.main_banner_item;
    }

    public class NetViewHolder extends BaseViewHolder<Integer>{
        public NetViewHolder( View itemView) {
            super(itemView);
        }
        @Override
        public void bindData(Integer data, int position, int pageSize) {


        }


    }
}
