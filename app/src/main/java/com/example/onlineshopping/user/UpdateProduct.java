package com.example.onlineshopping.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.CartData;
import com.example.onlineshopping.model.UserInformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.shalan.mohamed.itemcounterview.CounterListener;
import com.shalan.mohamed.itemcounterview.IncDecView;
import com.squareup.picasso.Picasso;

public class UpdateProduct extends AppCompatActivity {
    String productId , cartId , updateNameProduct , updateDescriptionProduct ,updateColorProduct, updateTotalPriceProduct ,updateImageIdProduct , updateQuantityProduct ;
    TextView name ,description , color , totalPrice , price ;
    Button updateBtn ;
    int quantity ,sumPrice;
    ImageView imageView ;
    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference mStorageRef;
    IncDecView incDecView ;
    CartData mCartData ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_product);
        name = findViewById(R.id.update_name);
        description = findViewById(R.id.update_description);
        color = findViewById(R.id.update_color);
        price = findViewById(R.id.update_price);
        imageView = findViewById(R.id.update_image);
        totalPrice = findViewById(R.id.update_totalPrice);
        incDecView = findViewById(R.id.itemCounter1);
        totalPrice = findViewById(R.id.update_totalPrice);
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef= database.getReference("myCart");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        getData();
        counterView();
    }
    private void getData()
    {
        Intent intent = getIntent();
        productId = intent.getStringExtra("id");
        cartId = intent.getStringExtra("cartId");
        updateNameProduct = intent.getStringExtra("updateName");
        updateDescriptionProduct  = intent.getStringExtra("updateDescription");
        updateColorProduct  = intent.getStringExtra("updateColor");
        updateTotalPriceProduct  = intent.getStringExtra("updatePrice");
        updateQuantityProduct  = intent.getStringExtra("updateQuantity");
        updateImageIdProduct  = intent.getStringExtra("updateImage");
        name.setText(updateNameProduct);
        description.setText(updateDescriptionProduct);
        color.setText(updateColorProduct);
        totalPrice.setText(updateTotalPriceProduct);
        int priceOfProduct = Integer.parseInt(updateTotalPriceProduct)/Integer.parseInt(updateQuantityProduct);
        price.setText(String.valueOf(priceOfProduct));
        Picasso.get().load(updateImageIdProduct).into(imageView);
    }
    private void counterView(){
        incDecView.setIncButtonIcon(R.drawable.ic_add);
        incDecView.setDecButtonIcon(R.drawable.ic_remove);
        incDecView.setBorderColor(android.R.color.darker_gray);
        incDecView.setBorderWidth(R.dimen.inc_dec_counter_view_stroke_width);
        incDecView.setStartCounterValue(updateQuantityProduct);
        incDecView.setCounterListener(new CounterListener() {
            @Override
            public void onIncClick(String value) {
                quantity = Integer.parseInt(value);
                sumPrice = Integer.parseInt(price.getText().toString()) + Integer.parseInt(totalPrice.getText().toString()) ;
                totalPrice.setText(String.valueOf(sumPrice));
                updateProduct();
                Toast.makeText(UpdateProduct.this, "The quantity has been Increased" , Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onDecClick(String value) {
                if (Integer.parseInt(price.getText().toString())!=Integer.parseInt(totalPrice.getText().toString()))
                {
                    quantity = Integer.parseInt(value);
                    sumPrice = Integer.parseInt(totalPrice.getText().toString()) -Integer.parseInt(price.getText().toString())  ;
                    totalPrice.setText(String.valueOf(sumPrice));
                    updateProduct();
                    Toast.makeText(UpdateProduct.this, "The quantity has been Decremented" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updateProduct()
    {
        mCartData = new CartData(productId , updateNameProduct , updateColorProduct ,sumPrice ,updateImageIdProduct,quantity , UserInformation.userEmail , cartId ,updateDescriptionProduct );
        myRef.child(cartId).setValue(mCartData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(UpdateProduct.this , HomeActivity.class);
        startActivity(i);
    }
}