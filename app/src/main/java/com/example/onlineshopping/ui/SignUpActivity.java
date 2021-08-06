package com.example.onlineshopping.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineshopping.R;
import com.example.onlineshopping.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.util.Calendar;

public class SignUpActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    Button birthdateBtn , addressBtn , signupBtn;
    TextView birthdateTv , addressTv;
    EditText usernameEdit , emailEdit , passwordEdit , phoneEdit , jobEdit ;
    RadioButton maleBtn , femaleBtn , adminBtn , userBtn ;
    RadioGroup radioGroup , radioGroup2;
    private FirebaseAuth mAuth;
    DatabaseReference myRef;
    FirebaseDatabase database;
    String gender ;
    String type ;
    Intent intent ;
    String address ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        birthdateBtn = findViewById(R.id.btnBirthDate);
        addressBtn = findViewById(R.id.btnAddress);
        signupBtn = findViewById(R.id.btnSignUp);
        birthdateTv = findViewById(R.id.tv_BirthDate);
        addressTv = findViewById(R.id.tv_Address);
        usernameEdit = findViewById(R.id.userName);
        emailEdit = findViewById(R.id.emailSignUp);
        passwordEdit = findViewById(R.id.passwordSignUp);
        phoneEdit = findViewById(R.id.phone);
        jobEdit = findViewById(R.id.job);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup2 = findViewById(R.id.radioGroup2);
        maleBtn = findViewById(R.id.maleRadioButton);
        femaleBtn = findViewById(R.id.femaleRadioButton);
        adminBtn = findViewById(R.id.adminRadioButton);
        userBtn = findViewById(R.id.userRadioButton);
        intent = getIntent() ;
        address = intent.getStringExtra("address");
        addressTv.setText(address);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://online-shopping-7d9f0-default-rtdb.firebaseio.com/");
        myRef = database.getReference("Accounts");
        birthdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new Date();
                dialogFragment.show(getSupportFragmentManager(),"date picker");
            }
        });
        addressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(SignUpActivity.this , MapAddressActivity.class);
                startActivity(intent);
            }
        });
        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    private void register()
    {
            String userName = usernameEdit.getText().toString().trim();
            String email = emailEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            String phone = phoneEdit.getText().toString().trim();
            String job = jobEdit.getText().toString().trim();
            addressTv.setText(address);
            String birthDate = birthdateTv.getText().toString().trim();
            int selectedId = radioGroup.getCheckedRadioButtonId();
             gender = "" ;
            if (selectedId == femaleBtn.getId()) {
                gender = "Female";
            } else if (selectedId == maleBtn.getId()) {
                gender = "Male";
            }
        int selectedId2 = radioGroup2.getCheckedRadioButtonId();
        type = "" ;
        if (selectedId2 == adminBtn.getId()) {
            type = "Admin";
        } else if (selectedId2 == userBtn.getId()) {
            type = "User";
        }
            if (userName.isEmpty()) {
                usernameEdit.setError("Enter Your Name");
                return;
            }
            if (email.isEmpty()) {
                emailEdit.setError("Enter Your E_mail");
                return;
            }
            if (password.isEmpty()) {
                passwordEdit.setError("Enter Your Password");
                return;
            }
            if (phone.isEmpty()) {
                phoneEdit.setError("Enter Your phone");
                return;
            }
            if (job.isEmpty()) {
                jobEdit.setError("Enter Your job");
                return;
            }
            if (!femaleBtn.isChecked() && !maleBtn.isChecked()) {
                femaleBtn.setError("not checked");
                maleBtn.setError("not checked");
                return;
            }
            mAuth.createUserWithEmailAndPassword(email , password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Users users = new Users(userName , email  , phone , address , job , birthDate , gender , type);
                    myRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(SignUpActivity.this,"User created successful" ,Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(SignUpActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
                }
            });
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        birthdateTv.setText(currentDate);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(SignUpActivity.this , MainActivity.class);
        startActivity(i);
    }
}

