package com.example.onlineshopping.user;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.onlineshopping.R;
import com.example.onlineshopping.adapters.CartAdapter;
import com.example.onlineshopping.model.CartData;
import com.example.onlineshopping.model.Order;
import com.example.onlineshopping.model.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
public class MyCartActivity extends AppCompatActivity implements CartAdapter.ItemOnClickListener {
    ImageView imageView;
    TextView textView , totalPriceText , showTotalPriceTextView ;
    Button checkoutBtn ;
    RecyclerView recyclerView;
    CartAdapter mCartAdapter;
    ArrayList<CartData> mCartData;
    ArrayList<CartData> cartDataArrayList ;
    DatabaseReference myRef , myRefOrder , myRefProduct;
    FirebaseDatabase database;
    StorageReference mStorageRef;
    FirebaseStorage mStorageRef1;

    Order orderData;
    int totalPrice;
    String orderDate;
    Calendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart);
        imageView = findViewById(R.id.imageCart);
        textView = findViewById(R.id.textCart);
        totalPriceText = findViewById(R.id.totalPriceTextView);
        showTotalPriceTextView = findViewById(R.id.textView7);
        checkoutBtn = findViewById(R.id.checkOutBtn);
        recyclerView = findViewById(R.id.myCartRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCartData = new ArrayList<>();
        cartDataArrayList = new ArrayList<>();
        mCartAdapter = new CartAdapter(mCartData, this, this);
        recyclerView.setAdapter(mCartAdapter);
        mStorageRef1 = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef = database.getReference("myCart");
        myRefOrder = database.getReference("order");
        myRefProduct = database.getReference("products");
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCartData.clear();
                totalPrice=0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("userEmail").getValue().toString().equals(UserInformation.userEmail)) {
                        mCartData.add(new CartData(ds.child("id").getValue().toString(),
                                ds.child("name").getValue().toString(),
                                ds.child("color").getValue().toString(),
                                Integer.parseInt(ds.child("price").getValue().toString()),
                                ds.child("imageId").getValue().toString(),
                                Integer.parseInt(ds.child("quantity").getValue().toString()),
                                ds.child("cartKey").getValue().toString(),
                                ds.child("description").getValue().toString()
                        ));
                        cartDataArrayList.add(new CartData(ds.child("id").getValue().toString(),
                                ds.child("name").getValue().toString(),
                                ds.child("color").getValue().toString(),
                                Integer.parseInt(ds.child("price").getValue().toString()),
                                ds.child("imageId").getValue().toString(),
                                Integer.parseInt(ds.child("quantity").getValue().toString())
                        ));
                        mCartAdapter.notifyDataSetChanged();
                        totalPrice += Integer.parseInt(ds.child("price").getValue().toString());
                    }
                    totalPriceText.setText(String.valueOf(totalPrice));
                    visible();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar= Calendar.getInstance();
                orderDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                orderData = new Order(UserInformation.userEmail, UserInformation.userAddress,UserInformation.username,UserInformation.userPhone, orderDate,totalPrice,cartDataArrayList);
                String orderId = myRefOrder.push().getKey();
                myRefOrder.child(orderId).setValue(orderData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MyCartActivity.this, "Order is Submitted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(MyCartActivity.this, HomeActivity.class));
                            deleteMyCart();
                        }
                    }
                });
            }
        });
    }
    private void deleteMyCart() {
        mCartData.clear();
        myRef.orderByChild("userEmail").equalTo(UserInformation.userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError databaseError) {

            }
        });
    }
    private void visible()
    {
        if (mCartAdapter.getItemCount() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            recyclerView.setAdapter(mCartAdapter);
            checkoutBtn.setVisibility(View.VISIBLE);
            totalPriceText.setVisibility(View.VISIBLE);
            showTotalPriceTextView.setVisibility(View.VISIBLE);
        } else {
            checkoutBtn.setVisibility(View.GONE);
            totalPriceText.setVisibility(View.GONE);
            showTotalPriceTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deleteProduct(position);
                    visible();
                    break;
                case ItemTouchHelper.RIGHT:
                    updateProduct(position);
                    break;
                default:
                    break;
            }
        }
        @Override
        public void onChildDraw(@NonNull @NotNull Canvas c, @NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MyCartActivity.this, R.color.white))
                    .addSwipeLeftActionIcon(R.drawable.removecart)
                    .addSwipeLeftLabel("Delete")
                    .setSwipeLeftLabelColor(R.color.yellow)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(MyCartActivity.this, R.color.white))
                    .addSwipeRightActionIcon(R.drawable.editcart)
                    .addSwipeRightLabel("Update")
                    .setSwipeRightLabelColor(R.color.yellow)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
    @Override
    public void onItemClick(int position) {
    }
    private void deleteProduct(int position) {
        final CartData cartData = mCartData.get(position);
        myRef.child(cartData.getCartKey()).removeValue();
        totalPrice-=cartData.getPrice();
        mCartData.remove(position);
        mCartAdapter.notifyItemRemoved(position);
    }
    private void updateProduct(int position) {
        final CartData cartData = mCartData.get(position);
        Intent i = new Intent(MyCartActivity.this, UpdateProduct.class);
        i.putExtra("id", cartData.getId());
        i.putExtra("cartId", cartData.getCartKey());
        i.putExtra("updateName", cartData.getName());
        i.putExtra("updateColor", cartData.getColor());
        i.putExtra("updateQuantity",String.valueOf(cartData.getQuantity()));
        i.putExtra("updatePrice", String.valueOf(cartData.getPrice()));
        i.putExtra("updateDescription", cartData.getDescription());
        i.putExtra("updateImage", cartData.getImageId());
        startActivity(i);
    }
}