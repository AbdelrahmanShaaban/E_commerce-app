package com.example.onlineshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.CartData;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.myViewHolder> {
    ArrayList<CartData> cartData ;
    Context cont ;
    private ItemOnClickListener listener ;

    public CartAdapter(ArrayList<CartData> cartData, Context cont ,ItemOnClickListener listener ) {
        this.cartData = cartData;
        this.cont = cont;
        this.listener = listener;
    }

    @Override
    public myViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_list_item,parent,false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull  myViewHolder holder, int position) {
        holder.name.setText(cartData.get(position).getName());
        holder.quantity.setText(String.valueOf(cartData.get(position).getQuantity()));
        holder.color.setText(cartData.get(position).getColor());
        holder.price.setText(String.valueOf(cartData.get(position).getPrice()));
        Picasso.get().load(cartData.get(position).getImageId()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {

        return (null != cartData ? cartData.size() : 0);
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView name ,quantity , color , price  ;
        ImageView imageView ;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameOfCart);
            quantity = itemView.findViewById(R.id.quantityOfCart);
            color = itemView.findViewById(R.id.colorOfCart);
            price = itemView.findViewById(R.id.priceOfCart);
            imageView = itemView.findViewById(R.id.imageOfCart);
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
