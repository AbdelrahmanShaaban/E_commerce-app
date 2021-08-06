package com.example.onlineshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.CategoryData;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemHolder> {
    Context context ;
    ArrayList<CategoryData> arrayList ;
    private ItemOnClickListener listener ;

    public CategoryAdapter(Context context , ArrayList<CategoryData> arrayList , ItemOnClickListener listener) {
        this.context = context ;
        this.arrayList = arrayList ;
        this.listener = listener;


    }

    @Override
    public ItemHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout_list_item , parent , false));
    }

    @Override
    public void onBindViewHolder( CategoryAdapter.ItemHolder holder, int position) {
        holder.textView.setText(arrayList.get(position).getTitle());
        holder.imageView.setImageResource(arrayList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView ;
        TextView textView ;
        public ItemHolder( View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.categoryImage);
            textView = itemView.findViewById(R.id.category_tv);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

        }
    }
    public interface ItemOnClickListener{
        void onItemClick( int position) ;
    }
}
