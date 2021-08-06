package com.example.onlineshopping.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.onlineshopping.R;
import com.example.onlineshopping.adapters.OrderProductAdapter;
import com.example.onlineshopping.model.CartData;
import com.example.onlineshopping.model.Order;
import com.example.onlineshopping.model.UserInformation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ShowOrderProductsActivity extends AppCompatActivity {
    RecyclerView recyclerView ;
    OrderProductAdapter orderProductAdapter;
    ArrayList<CartData> cartData ;
    ArrayList<Order> orderData ;
    DatabaseReference myRef ;
    FirebaseDatabase database;
    Button sentOrder ;
    String userEmail , orderDate ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_order_products);
        sentOrder = findViewById(R.id.sentOrderBtn);
        recyclerView = findViewById(R.id.showOrderRecyclerView) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartData=new ArrayList<>();
        orderData = new ArrayList<>();
        orderProductAdapter = new OrderProductAdapter(cartData , this );
        recyclerView.setAdapter(orderProductAdapter);
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef = database.getReference("order");
        Intent intent = getIntent();
         orderDate=intent.getStringExtra("orderDate");
         userEmail=intent.getStringExtra("userEmail");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cartData.clear();
                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    if(ds.child("userEmail").getValue().toString().equals(userEmail)&&ds.child("orderDate").getValue().toString().equals(orderDate)) {
                        for (DataSnapshot dataSnapshot1:ds.child("cartDataArrayList").getChildren())
                        {
                            cartData.add(new CartData(
                                    dataSnapshot1.child("id").getValue().toString(),
                                    dataSnapshot1.child("name").getValue().toString(),
                                    dataSnapshot1.child("color").getValue().toString(),
                                    Integer.parseInt(dataSnapshot1.child("price").getValue().toString()),
                                    dataSnapshot1.child("imageId").getValue().toString(),
                                    Integer.parseInt(dataSnapshot1.child("quantity").getValue().toString())));
                            orderProductAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        sentOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sentConfirmEmail();
                SimpleDateFormat formatter=new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
                Calendar calendar1 = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                myRef.orderByChild("userEmail").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds: dataSnapshot.getChildren()) {
                            try {
                                calendar1.setTime(formatter.parse(ds.child("orderDate").getValue().toString()));
                                calendar2.setTime(formatter.parse(orderDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            java.util.Date date1 = calendar1.getTime();
                            Date date2 = calendar2.getTime();
                            if (date1.compareTo(date2) == 0)
                            {
                                ds.getRef().removeValue();
                                //databaseOrder.child(ds.getKey().toString()).removeValue();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

                    }
                });
              /*  myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(ds.child("userEmail").getValue().toString().equals(userEmail))
                            {
                                try {
                                    calendar1.setTime(formatter.parse(ds.child("orderDate").getValue().toString()));
                                    calendar2.setTime(formatter.parse(orderDate));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                java.util.Date date1 = calendar1.getTime();
                                Date date2 = calendar2.getTime();
                                if (date1.compareTo(date2) == 0)
                                {
                                   ds.getRef().removeValue();
                                    //databaseOrder.child(ds.getKey().toString()).removeValue();
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });*/
            }
        });
    }
    private void sentConfirmEmail() {
        SimpleDateFormat formatter = new SimpleDateFormat("E, MMM dd yyyy HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        String addcurrentDateThreeDays, addcurrentDateFiveDays;
        try {
            calendar.setTime(formatter.parse(orderDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.DATE, 3);
        addcurrentDateThreeDays = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        calendar.add(Calendar.DATE, 2);
        addcurrentDateFiveDays = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent.putExtra(Intent.EXTRA_SUBJECT, "Online Shopping");
        intent.putExtra(Intent.EXTRA_TEXT, "Your order has been purchased and is expected to be delivered from " + addcurrentDateThreeDays + " to " + addcurrentDateFiveDays + ".");
        intent.setData(Uri.parse("mailto:" + userEmail)); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShowOrderProductsActivity.this , AdminActivity.class);
        startActivity(intent);
    }
}