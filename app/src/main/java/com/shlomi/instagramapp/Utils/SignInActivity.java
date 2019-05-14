package com.shlomi.instagramapp.Utils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.shlomi.instagramapp.Home.Home;
import com.shlomi.instagramapp.R;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private  EditText emailText;
    private  EditText passwordText;
    private  TextView registerTextView;
    private Button buttonSignIn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

         emailText = (EditText) findViewById(R.id.editTextMail);
         passwordText = (EditText) findViewById(R.id.editTextPassword);
         buttonSignIn = (Button)findViewById(R.id.buttonSignIn);
        registerTextView = (TextView)findViewById(R.id.textViewRegister);
        buttonSignIn.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void signIn(){
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email.",Toast.LENGTH_SHORT).show();

            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please Enter password.",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Waiting...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
               if(task.isSuccessful()){
                   Toast.makeText(SignInActivity.this,"Log In Success.",Toast.LENGTH_SHORT).show();

                 //startActivity(new Intent(getApplicationContext(),new ProfileActivity().getClass()));Intent i = new Intent(CurrentClassName.this, LoginActivity.class);
                   Intent intent = new Intent(SignInActivity.this, Home.class);
                  // finish();
                   startActivity(intent);
               }else{
                   Toast.makeText(SignInActivity.this,"Log In Faild,Try Again..",Toast.LENGTH_SHORT).show();

                    //Toast.makeText()
               }
            }
        });
    }

    @Override
    public void onClick(View view){
        if (view == buttonSignIn){
            signIn();
        }
        if (view == registerTextView){
            finish();
            startActivity(new Intent(this,MainActivity.class));
            System.out.print("here!!");
         }
    }
}
