package com.example.onlineshopping.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.onlineshopping.ui.CaptureCode;
import com.example.onlineshopping.model.ProductData;
import com.example.onlineshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

public class AdminActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {
    TextView getBarcodeTv ;
    Button addProductBtn , scanProductBtn , button;
    EditText nameOfProduct,desOfProduct,colorOfProduct,priceOfProduct,catOfProduct;
    ImageView imageOfProduct;
    StorageTask uploadTask  ;
    Uri imageUri ;
    String category ;
    private StorageReference mStorageRef;
    DatabaseReference myRef2;
    FirebaseDatabase database;
    Spinner spinner ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getBarcodeTv = findViewById(R.id.getCodeTextView);
        nameOfProduct = findViewById(R.id.productName);
        desOfProduct = findViewById(R.id.productDescription);
        colorOfProduct = findViewById(R.id.productColor);
        priceOfProduct = findViewById(R.id.productPrice);
        imageOfProduct = findViewById(R.id.imageProduct);
        addProductBtn = findViewById(R.id.btnProduct);
        scanProductBtn = findViewById(R.id.btnScanProduct);
        mStorageRef = FirebaseStorage.getInstance().getReference("Images");
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef2 = database.getReference("products");
        spinner = findViewById(R.id.categories_spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        imageOfProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChooser();
            }
        });
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask!=null&&uploadTask.isInProgress())
                {
                    Toast.makeText(AdminActivity.this,"Upload in progress",Toast.LENGTH_LONG).show();
                }else{
                    fileUploader();
                }
            }
        });
        scanProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanBarCode();
            }
        });
        button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminActivity.this, OrderActivity.class);
                startActivity(i);
            }
        });
    }
    private void scanBarCode() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CaptureCode.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scanning Code");
        integrator.initiateScan();
    }
    private String getExtension(Uri uri)
    {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }
    private void fileUploader() {
        String imageId = System.currentTimeMillis()+"."+getExtension(imageUri);
        String name =nameOfProduct.getText().toString().trim() ;
        String description =desOfProduct.getText().toString().trim() ;
        String color =colorOfProduct.getText().toString().trim() ;
        String barcode = getBarcodeTv.getText().toString().trim();
        int price =Integer.parseInt(priceOfProduct.getText().toString().trim()) ;
        if (name.isEmpty()){
            nameOfProduct.setError("Name is Required");
            return;
        }
        if (description.isEmpty()){
            desOfProduct.setError("Description is Required");
            return;
        }
        if (color.isEmpty()){
            colorOfProduct.setError("Color is Required");
            return;
        }
        if (priceOfProduct.getText().toString().trim().isEmpty()){
            priceOfProduct.setError("Price is Required");
            return;
        }
        if(imageUri != null)
        {
            StorageReference filReference = mStorageRef.child(imageId);
            filReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Toast.makeText(AdminActivity.this, "uploaded successfully", Toast.LENGTH_SHORT).show();
                                    String productId = myRef2.push().getKey();
                                    ProductData data = new ProductData(productId,name, color, price, uri.toString(), description, category , barcode);
                                    myRef2.child(productId).setValue(data);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(AdminActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
        }else {
            Toast.makeText(this,"No File Selected" , Toast.LENGTH_LONG).show();
        }
    }
    private void fileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent , 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode , resultCode , data);
        if (requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null)
        {
            imageUri = data.getData();
            imageOfProduct.setImageURI(imageUri);
        }
        if (result!=null) {
            if (result.getContents() != null) {
                getBarcodeTv.setText(result.getContents());
            } else {
                Toast.makeText(AdminActivity.this, "Not Result", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        category= adapterView.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}