package com.example.onlineshopping.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopping.R;
import com.example.onlineshopping.admin.AdminActivity;
import com.example.onlineshopping.model.UserInformation;
import com.example.onlineshopping.user.HomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    Button  registerBtn;
    FloatingActionButton loginBtn ;
    TextView forgotTv ;
    TextInputEditText emailLogin , passwordLogin ;
    CheckBox checkBox ;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    CheckBox rememberMeCheckBox;
    SharedPreferences sharedPreferences;
    private static final String PREFS_NAME="PrefsFile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerBtn = findViewById(R.id.btnRegister);
        loginBtn = findViewById(R.id.btnLogin);
        forgotTv = findViewById(R.id.forgotTextView);
        emailLogin = findViewById(R.id.emailLogin);
        rememberMeCheckBox = findViewById(R.id.checkBox);
        passwordLogin = findViewById(R.id.passwordLogin);
        checkBox = findViewById(R.id.checkBox);
        sharedPreferences =getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef = database.getReference("Accounts");
        forgotTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , ForgetActivity.class);
                startActivity(i);
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , SignUpActivity.class);
                startActivity(i);
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    login();
            }
        });
        getPreferencesData() ;
    }
    private void login() {
        String email = emailLogin.getText().toString().trim();
        String password = passwordLogin.getText().toString().trim();
        if (email.isEmpty()) {
            emailLogin.setError("Enter Your E_mail");
        }
        if (password.isEmpty()) {
            passwordLogin.setError("Enter Your Password");
        }
            mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        RememberMe();
                        Query mQuery = myRef.getRef().orderByChild("email").equalTo(email);
                        mQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    if (ds.child("type").getValue().toString().equals("Admin")) {
                                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                                        startActivity(intent);
                                    } else if (ds.child("type").getValue().toString().equals("User")) {
                                        UserInformation.username = ds.child("userName").getValue().toString();
                                        UserInformation.userEmail = ds.child("email").getValue().toString();
                                        UserInformation.userPhone = ds.child("phone").getValue().toString();
                                        UserInformation.userAddress = ds.child("address").getValue().toString();

                                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    } else {
                        Toast.makeText(MainActivity.this, "login failed", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    private void RememberMe() {
        if(rememberMeCheckBox.isChecked())
        {
            Boolean boolIsChecked=rememberMeCheckBox.isChecked();
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("pref_email",emailLogin.getText().toString());
            editor.putString("pref_pass",passwordLogin.getText().toString());
            editor.putBoolean("pref_check",boolIsChecked);
            editor.apply();
        }
        else
        {
            sharedPreferences.edit().clear().apply();
        }
    }
    private void getPreferencesData() {
        SharedPreferences SP=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        if(SP.contains("pref_email")){
            String name=SP.getString("pref_email","not found");
            emailLogin.setText(name.toString());
        }
        if(SP.contains("pref_pass")){
            String password=SP.getString("pref_pass","not found");
            passwordLogin.setText(password.toString());
        }
        if(SP.contains("pref_check")){
            Boolean check=SP.getBoolean("pref_check",false);
            rememberMeCheckBox.setChecked(check);
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}


