package com.example.onlineshopping.user;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.onlineshopping.R;
import com.example.onlineshopping.adapters.AllProductAdapter;
import com.example.onlineshopping.model.ProductData;
import com.example.onlineshopping.ui.CaptureCode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.iammert.library.ui.multisearchviewlib.MultiSearchView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Locale;

public class SearchProduct extends AppCompatActivity implements  AllProductAdapter.ItemOnClickListener  {
    private static final int voiceCode=1;
    ImageButton micBtn ;
    ImageButton cameraBtn ;
    RecyclerView recyclerView ;
    AllProductAdapter mProductAdapter ;
    ArrayList<ProductData> mProductData ;
    DatabaseReference myRef;
    FirebaseDatabase database;
    StorageReference mStorageRef;
    MultiSearchView multiSearchView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_search_product);
        DataBindingUtil.setContentView(this, R.layout.activity_search_product);
        micBtn = findViewById(R.id.search_image_view_mic);
        cameraBtn = findViewById(R.id.search_image_view_photo);
        recyclerView = findViewById(R.id.allProductRecycler) ;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProductData = new ArrayList<>();
        mProductAdapter = new AllProductAdapter(mProductData , this , this);
        mProductAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(mProductAdapter);
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef= database.getReference("products");
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        multiSearchView = findViewById(R.id.multiSearchView);
        micBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talk();
            }
        });
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds :dataSnapshot.getChildren())
                {
                    mProductData.add(new ProductData(ds.child("id").getValue().toString(),
                            ds.child("name").getValue().toString(),
                            ds.child("color").getValue().toString(),
                            Integer.parseInt(ds.child("price").getValue().toString()),
                            ds.child("imageId").getValue().toString(),
                            ds.child("description").getValue().toString(),
                            ds.child("category").getValue().toString(),
                            ds.child("barcode").getValue().toString()));
                        mProductAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        });
        searchProduct();
    }
    @Override
    public void onItemClick(int position) {
        Intent i = new Intent(SearchProduct.this , ShowProductActivity.class);
        i.putExtra("id" , mProductData.get(position).getId());
        i.putExtra("name" , mProductData.get(position).getName());
        i.putExtra("color" , mProductData.get(position).getColor());
        i.putExtra("disc" , mProductData.get(position).getDescription());
        i.putExtra("price" , String.valueOf(mProductData.get(position).getPrice()));
        i.putExtra("image" , mProductData.get(position).getImageId());
        startActivity(i);
    }
    private void talk(){
        Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Product name");
        startActivityForResult(intent,voiceCode);
    }
    private void scan(){
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureCode.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult resultScan = IntentIntegrator.parseActivityResult(requestCode , resultCode , data);
        switch (requestCode) {
            case voiceCode:{
                if (resultCode==RESULT_OK && null!=data)
                {
                    ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    mProductAdapter.getFilter().filter(result.get(0));
                }
            }
            break;
        }
        if (resultScan!=null)
        {
            if (resultScan.getContents()!=null)
            {
                mProductAdapter.getFilter().filter(resultScan.getContents());
            }
            else{
                Toast.makeText(SearchProduct.this ,"Not Result",Toast.LENGTH_LONG).show();
            }
        }
    }
    private void searchProduct()
    {
        multiSearchView.setSearchViewListener(new MultiSearchView.MultiSearchViewListener() {
            @Override
            public void onTextChanged(int i, @NotNull CharSequence charSequence) {
                mProductAdapter.getFilter().filter(charSequence);
            }
            @Override
            public void onSearchComplete(int i, @NotNull CharSequence charSequence) {
            }
            @Override
            public void onSearchItemRemoved(int i) {
            }
            @Override
            public void onItemSelected(int i, @NotNull CharSequence charSequence) {
               mProductAdapter.getFilter().filter(charSequence);
            }
        });
    }
}