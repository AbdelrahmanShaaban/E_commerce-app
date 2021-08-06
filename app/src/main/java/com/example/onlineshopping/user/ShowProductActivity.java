package com.example.onlineshopping.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.CartData;
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
import com.shalan.mohamed.itemcounterview.CounterListener;
import com.shalan.mohamed.itemcounterview.IncDecView;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

public class ShowProductActivity extends AppCompatActivity {
     TextView name ,description , color , price , totalPrice ;
     ImageView imageView ;
     IncDecView incDecView ;
     Button addToCart ;
    String id , nameProduct , descriptionProduct ,colorProduct,priceProduct ,imageIdProduct , cartKey ;
    int sumPrice , needQuantity;
    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference mStorageRef;
    CartData mCartData ;
    CardView cardView ;
    boolean first;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_product);
        cardView = findViewById(R.id.cardView);
        name = findViewById(R.id.product_name);
        description = findViewById(R.id.product_description);
        color = findViewById(R.id.product_color);
        price = findViewById(R.id.product_price);
        imageView = findViewById(R.id.product_image);
        incDecView = findViewById(R.id.itemCounter);
        totalPrice = findViewById(R.id.totalPrice);
        addToCart = findViewById(R.id.btnAddCart);
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef= database.getReference("myCart");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        first=true;
        getData();
        checkExistProductInMyCart();
    }
    private void checkExistProductInMyCart() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(ds.child("userEmail").getValue().toString().equals(UserInformation.userEmail)&&ds.child("id").getValue().toString().equals(id))
                    {
                        counterView(ds.child("quantity").getValue().toString());
                        totalPrice.setText(ds.child("price").getValue().toString());
                        cartKey = ds.child("cartKey").getValue().toString();
                        first=false;
                        break;
                    }
                }
                if(first==true) {
                    cardView.setVisibility(View.GONE);
                    addMyProductToDatabase();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });
    }
    private void getData()
    {
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        nameProduct = intent.getStringExtra("name");
        descriptionProduct  = intent.getStringExtra("disc");
        colorProduct  = intent.getStringExtra("color");
        priceProduct  = intent.getStringExtra("price");
        imageIdProduct  = intent.getStringExtra("image");
        name.setText(nameProduct);
        description.setText(descriptionProduct);
        color.setText(colorProduct);
        price.setText(priceProduct);
        totalPrice.setText(priceProduct);
        Picasso.get().load(imageIdProduct).into(imageView);
    }
    private void counterView(String quantity){
        addToCart.setVisibility(View.GONE);
        incDecView.setIncButtonIcon(R.drawable.ic_add);
        incDecView.setDecButtonIcon(R.drawable.ic_remove);
        incDecView.setBorderColor(android.R.color.darker_gray);
        incDecView.setBorderWidth(R.dimen.inc_dec_counter_view_stroke_width);
        incDecView.setStartCounterValue(quantity);
        incDecView.setCounterListener(new CounterListener() {
            @Override
            public void onIncClick(String value) {
                needQuantity = Integer.parseInt(value);
                sumPrice = Integer.parseInt(price.getText().toString()) + Integer.parseInt(totalPrice.getText().toString()) ;
                totalPrice.setText(String.valueOf(sumPrice));
                editProductInDatabase(sumPrice,needQuantity);
                Toast.makeText(ShowProductActivity.this, "The quantity has been Increased" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDecClick(String value) {
                if (Integer.parseInt(price.getText().toString())!=Integer.parseInt(totalPrice.getText().toString()))
                {
                    needQuantity = Integer.parseInt(value);
                    sumPrice = Integer.parseInt(totalPrice.getText().toString()) -Integer.parseInt(price.getText().toString())  ;
                    totalPrice.setText(String.valueOf(sumPrice));
                    editProductInDatabase(sumPrice,needQuantity);
                    Toast.makeText(ShowProductActivity.this, "The quantity has been Decremented" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addMyProductToDatabase()
    {
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cartId = myRef.push().getKey() ;
                mCartData = new CartData( id ,nameProduct , colorProduct ,Integer.parseInt(priceProduct) ,imageIdProduct,1 ,UserInformation.userEmail , cartId , descriptionProduct );
                myRef.child(cartId).setValue(mCartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ShowProductActivity.this,"Product is added" ,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                cardView.setVisibility(View.VISIBLE);
                counterView("1");
            }
        });
    }
    private void editProductInDatabase(int price,int quantity)
    {
        mCartData = new CartData(id , nameProduct , colorProduct ,price ,imageIdProduct,quantity ,UserInformation.userEmail , cartKey ,descriptionProduct);
        myRef.child(cartKey).setValue(mCartData);
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ShowProductActivity.this , HomeActivity.class);
        startActivity(i);
    }
}