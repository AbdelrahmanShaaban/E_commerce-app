package com.example.onlineshopping.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineshopping.model.CartData;
import com.example.onlineshopping.ui.MapActivity;
import com.example.onlineshopping.model.Order;
import com.example.onlineshopping.adapters.OrderAdapter;
import com.example.onlineshopping.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity implements OrderAdapter.ItemOnClickListener {
    RecyclerView recyclerView ;
    OrderAdapter orderAdapter;
    ArrayList<Order> orderData ;
    ArrayList<CartData> cartData ;
    DatabaseReference myRef ;
    FirebaseDatabase database;
    Address mAddress ;
    ImageView imageView;
    TextView textView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        imageView = findViewById(R.id.imageOrder);
        textView = findViewById(R.id.textOrder);
        recyclerView = findViewById(R.id.orderRecyclerView) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderData = new ArrayList<>();
        cartData=new ArrayList<>();
        orderAdapter = new OrderAdapter(orderData , this , this);

        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef = database.getReference("order");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderData.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    for (DataSnapshot dataSnapshot1:ds.child("cartDataArrayList").getChildren())
                    {
                        cartData.add(new CartData(
                                dataSnapshot1.child("id").getValue().toString(),
                                dataSnapshot1.child("name").getValue().toString(),
                                dataSnapshot1.child("color").getValue().toString(),
                                Integer.parseInt(dataSnapshot1.child("price").getValue().toString()),
                                dataSnapshot1.child("imageId").getValue().toString(),
                                Integer.parseInt(dataSnapshot1.child("quantity").getValue().toString())));
                    }
                    orderData.add(new Order(
                            ds.child("userEmail").getValue().toString(),
                            ds.child("userAddress").getValue().toString(),
                            ds.child("username").getValue().toString(),
                            ds.child("userPhone").getValue().toString(),
                            ds.child("orderDate").getValue().toString(),
                            Integer.parseInt(ds.child("totalPrice").getValue().toString()),
                            cartData));
                    orderAdapter.notifyDataSetChanged();
                    visible();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
    }
    @Override
    public void showOrderProducts(int position) {
        Order mOrder = orderData.get(position);
        Intent i = new Intent(OrderActivity.this , ShowOrderProductsActivity.class);
        i.putExtra("userEmail",mOrder.getUserEmail());
        i.putExtra("orderDate",mOrder.getOrderDate());
        startActivity(i);
    }
    @Override
    public void showAddressInMap(int position) {
        Order mOrder = orderData.get(position);
        Geocoder geocoder = new Geocoder(getApplicationContext() , Locale.getDefault());
        try {
            List addressList = geocoder.getFromLocationName(mOrder.getUserAddress(),1);
            if (addressList!=null && addressList.size()>0){
                mAddress = (Address)addressList.get(0);
                Intent i = new Intent(OrderActivity.this , MapActivity.class);
                i.putExtra("latitude" , String.valueOf(mAddress.getLatitude())) ;
                i.putExtra("longitude" ,  String.valueOf(mAddress.getLongitude())) ;
                i.putExtra("address" ,  String.valueOf(mOrder.getUserAddress())) ;
                startActivity(i);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void visible() {
        if (orderAdapter.getItemCount() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            recyclerView.setAdapter(orderAdapter);

        } else {

            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }
}