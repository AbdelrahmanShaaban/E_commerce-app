package com.example.onlineshopping.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineshopping.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgetActivity extends AppCompatActivity {
    EditText resetEmail ;
    Button resetPassword ;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        resetEmail = findViewById(R.id.emailForgot);
        resetPassword = findViewById(R.id.btnResetPassword);
        mAuth = FirebaseAuth.getInstance();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = resetEmail.getText().toString().trim();
                if (mail.isEmpty()) {
                    resetEmail.setError("Enter the E_mail");
                }else{
                mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(ForgetActivity.this, "Reset link sent to your E_mail", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(ForgetActivity.this, "Error ! Reset link is not sent to your E_mail", Toast.LENGTH_LONG).show();

                    }
                });
            }
        }
        });

    }

}