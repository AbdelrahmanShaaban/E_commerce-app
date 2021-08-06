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

public class OrderProductAdapter extends RecyclerView.Adapter<OrderProductAdapter.myViewHolder> {
    ArrayList<CartData> cartData;
    Context cont ;


    public OrderProductAdapter(ArrayList<CartData> cartData, Context cont  ) {
        this.cartData = cartData;
        this.cont = cont;

    }

    @Override
    public OrderProductAdapter.myViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_show_order_item,parent,false);
        return new OrderProductAdapter.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductAdapter.myViewHolder holder, int position) {
        holder.name.setText(cartData.get(position).getName());
        holder.quantity.setText(String.valueOf(cartData.get(position).getQuantity()));
        holder.color.setText(cartData.get(position).getColor());
        holder.price.setText(String.valueOf(cartData.get(position).getPrice()));
        Picasso.get().load(cartData.get(position).getImageId()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return cartData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView name ,quantity , color , price  ;
        ImageView imageView ;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameOfProduct);
            quantity = itemView.findViewById(R.id.quantityOfProduct);
            color = itemView.findViewById(R.id.colorOfProduct);
            price = itemView.findViewById(R.id.priceOfProduct);
            imageView = itemView.findViewById(R.id.imageOfProduct);
        }
    }

}


