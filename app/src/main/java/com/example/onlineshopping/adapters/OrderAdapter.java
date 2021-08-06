package com.example.onlineshopping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.Order;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.myViewHolder> {
    ArrayList<Order> orderData ;
    Context cont ;
    private ItemOnClickListener listener ;

    public OrderAdapter(ArrayList<Order> orderData, Context cont , ItemOnClickListener listener ) {
        this.orderData=orderData;
        this.cont = cont;
        this.listener = listener;
    }
    @Override
    public myViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_item,parent,false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull myViewHolder holder, int position) {
        holder.userEmailTv.setText(orderData.get(position).getUserEmail());
        holder.userAddressTv.setText(orderData.get(position).getUserAddress());
        holder.userNameTv.setText(orderData.get(position).getUsername());
        holder.userPhoneTv.setText(orderData.get(position).getUserPhone());
        holder.orderDateTv.setText(orderData.get(position).getOrderDate());
        holder.totalPriceTv.setText(String.valueOf(orderData.get(position).getTotalPrice()));
        holder.userAddressTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.showAddressInMap(position);
            }
        });

    }
    @Override
    public int getItemCount() {
        return orderData.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView userEmailTv ,userAddressTv , userNameTv , userPhoneTv,orderDateTv,totalPriceTv;
        public myViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            userEmailTv = itemView.findViewById(R.id.userEmail);
            userAddressTv = itemView.findViewById(R.id.userAddress);
            userNameTv = itemView.findViewById(R.id.userName);
            userPhoneTv = itemView.findViewById(R.id.userPhone);
            orderDateTv = itemView.findViewById(R.id.orderDate);
            totalPriceTv = itemView.findViewById(R.id.totalPrice);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.showOrderProducts(getAdapterPosition());
                }
            });
        }
    }
    public interface ItemOnClickListener{
        void showOrderProducts( int position) ;
        void showAddressInMap( int position) ;
    }
}

