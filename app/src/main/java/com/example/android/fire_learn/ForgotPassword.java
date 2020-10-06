package com.example.android.fire_learn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.CompletionService;

public class ForgotPassword extends AppCompatActivity {
    Button sendLink;
    EditText mEmail;
    FirebaseAuth mFirebaseAuth;
    ConstraintLayout layout1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        sendLink=(Button) findViewById(R.id.send_password_reset_btn);
        mEmail=(EditText) findViewById(R.id.editTextTextEmailAddress);
        layout1=(ConstraintLayout) findViewById(R.id.reset_layout);
        mFirebaseAuth=FirebaseAuth.getInstance();
        sendLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmail.getText().toString().trim();
                if(Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {

                        mFirebaseAuth.sendPasswordResetEmail(email);
                        Snackbar.make(layout1, "Password reset link send! Please check your mail.", Snackbar.LENGTH_SHORT).show();
                    Intent transfer=new Intent(ForgotPassword.this,Login.class);
                    startActivity(transfer);
                    finish();
                }
            }
        });
    }
}