package com.example.android.fire_learn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText lEmail;
    EditText lPassword;
    Button lLogin;
    TextView lSignUp;
    TextView reset_link;
    FirebaseAuth mFirebaseAuth;
    ConstraintLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth=FirebaseAuth.getInstance();
        lEmail=(EditText) findViewById(R.id.login_email);
        lPassword=(EditText) findViewById(R.id.login_password);
        lLogin=(Button) findViewById(R.id.login);
        layout=(ConstraintLayout) findViewById(R.id.layout);
        reset_link=(TextView) findViewById(R.id.reset_link);
        lSignUp=(TextView) findViewById(R.id.intentLoginText);
        lSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent transfer=new Intent(Login.this,MainActivity.class);
                startActivity(transfer);
            }
        });
        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callLogin();
            }
        });
        reset_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent transfer=new Intent(Login.this,ForgotPassword.class);
               startActivity(transfer);
            }
        });
    }

    private void callLogin() {
        String email=lEmail.getText().toString().trim();
        String password=lPassword.getText().toString().trim();
        if(email.isEmpty())
        {
            Toast.makeText(this,"Enter your email!",Toast.LENGTH_SHORT).show();
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(this,"Enter a valid email address!",Toast.LENGTH_SHORT).show();
        }
        else if(password.isEmpty())
        {
            Toast.makeText(this,"Enter password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            final ProgressDialog progressDialog=new ProgressDialog(Login.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressDialog.dismiss();
                    if(task.isSuccessful())
                    {
                        FirebaseUser user=task.getResult().getUser();
                        startMainActivity(user);
                    }
                    else
                    {

                        if(task.getException() instanceof FirebaseAuthInvalidUserException)
                        {
                            createAlert("Error","This email is not registered","OK");
                        }
                        else if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                        {
                            createAlert("Error","Wrong Password!","OK");
                        }
                        else
                        {
                            Toast.makeText(Login.this,"Unable to login.",Toast.LENGTH_SHORT).show();
                            task.getException().printStackTrace();
                        }
                    }
                }
            });
        }
    }

    private void startMainActivity(FirebaseUser user) {
        if(user!=null)
        {
            Intent transfer=new Intent(this,Home.class);
            transfer.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(transfer);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mFirebaseAuth.getCurrentUser();
        startMainActivity(user);
    }

    private void createAlert(String heading, String message, String possitive)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(heading)
                .setMessage(message)
                .setPositiveButton(possitive,null)
                .create().show();
    }
}