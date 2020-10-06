package com.example.android.fire_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
    TextView mName;
    TextView mPhoneNum;
    Button mVerify;
    TextView mVerifyAlert;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mVerify=(Button) findViewById(R.id.resend_code);
        mVerifyAlert=(TextView) findViewById(R.id.alertTitle);
        mName=(TextView) findViewById(R.id.name_display);
        mPhoneNum=(TextView) findViewById(R.id.display_phone);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        if(!user.isEmailVerified())
        {
            mVerifyAlert.setVisibility(View.VISIBLE);
        }
        else
        {
            mVerify.setVisibility(View.GONE);
            mVerifyAlert.setVisibility(View.GONE);
        }
        mVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Home.this,"Verification E-mail has been sent. Please verify!",Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                        Intent transfer=new Intent(Home.this,Login.class);
                        startActivity(transfer);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Home.this,"Error in sending verfication e-mail",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        String name=FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        String phone=FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
        mName.setText(name);
        mPhoneNum.setText(phone);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.sign_out_menu:
            {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(Home.this,"Sign out!",Toast.LENGTH_SHORT).show();
                Intent transfer=new Intent(Home.this,Login.class);
                startActivity(transfer);
                finish();
            }
    }
    return super.onOptionsItemSelected(item);
    }
}