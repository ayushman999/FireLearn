package com.example.android.fire_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText mName;
    EditText mPassword;
    EditText mEmail;
    EditText mPhoneNum;
    Button mCreate;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    CollectionReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        mName=(EditText) findViewById(R.id.create_name);
        mEmail=(EditText) findViewById(R.id.create_email);
        mPassword=(EditText) findViewById(R.id.create_password);
        mPhoneNum=(EditText) findViewById(R.id.create_phone_num);
        mCreate=(Button) findViewById(R.id.create);
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String name=mName.getText().toString().trim();
        String email=mEmail.getText().toString().trim();
        String password=mPassword.getText().toString().trim();
        final String phoneNum=mPhoneNum.getText().toString().trim();
        if(name.equals(""))
        {
            Toast.makeText(this,"Enter your name!",Toast.LENGTH_SHORT).show();
        }
        else if(email.isEmpty())
        {
            Toast.makeText(this,"Please Enter your email",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Please enter a valid email address",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty())
        {
            Toast.makeText(this,"Enter a password!",Toast.LENGTH_SHORT).show();
        }
        else if(password.length()<6)
        {
            Toast.makeText(this,"Password must be greater than equal to 6 characters",Toast.LENGTH_SHORT).show();
        }
        else if(phoneNum.isEmpty())
        {
            Toast.makeText(this,"Enter your phone number!",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.PHONE.matcher(phoneNum).matches())
        {
            Toast.makeText(this,"Please enter a valid phone number.",Toast.LENGTH_SHORT).show();
        }
        else
        {
            final ProgressDialog progressDialog=new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("Registering...");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.setMessage("Adding Data...");
                    FirebaseUser user=authResult.getUser();
                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this,"Verification E-mail has been sent. Please verify!",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this,"Error in sending verfication e-mail",Toast.LENGTH_SHORT).show();
                        }
                    });
                    reference=firestore.collection(user.getUid());
                    Map<String,String> userData=new HashMap<>();
                    userData.put("contact",phoneNum);
                    userData.put("name",name);
                    reference.add(userData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"User data added!",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Unable to add user data",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this,"Unable to signup.",Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            });
        }
    }


}