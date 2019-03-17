package com.shlomi.instagramapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.*;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText emailText;
    private EditText passwordText;
    private TextView signInTextView;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        emailText = (EditText)findViewById(R.id.editTextMail);
        passwordText = (EditText) findViewById(R.id.editTextPassword);
        signInTextView = (TextView)findViewById(R.id.textViewSignIn);
        buttonRegister.setOnClickListener(this);
        signInTextView.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

    }

    private void registerUser(){
     final String email = emailText.getText().toString().trim();
     final String password = passwordText.getText().toString().trim();
     if(TextUtils.isEmpty(email)){
         Toast.makeText(this,"Please Enter Email.",Toast.LENGTH_SHORT).show();
         return;
     }
     if(TextUtils.isEmpty(password)){
         Toast.makeText(this,"Please Enter password.",Toast.LENGTH_SHORT).show();
       return;
     }

     progressDialog.setMessage("Please Wait...");
        progressDialog.show();
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               if(task.isSuccessful()){
                   Toast.makeText(MainActivity.this,"Register Succses , User Created",Toast.LENGTH_SHORT).show();
                   progressDialog.cancel();

                       finish();
                       startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                   firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid()).setValue(email.toString().trim());
                   firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid()).setValue(password);

               }else{
                   Toast.makeText(MainActivity.this,"Register faild , User not Created,try again.",Toast.LENGTH_SHORT).show();

               }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }else if(view == signInTextView){
            finish();
            startActivity(new Intent(this,SignInActivity.class));
         }
    }
}
