package com.shlomi.instagramapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.*;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText emailText;
    private EditText passwordText;
    private TextView signInTextView;
    private EditText userName;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private StorageReference storageReference ;
    private Storage storage;



    private Button btnChoose, btnUpload;
    private ImageView imageView;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

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
        userName = (EditText)findViewById(R.id.editUserName);

        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        imageView = (ImageView) findViewById(R.id.imgView);
        storageReference = storage.getReference();
        storage = FirebaseStorage.getInstance();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void registerUser(){
     final String email = emailText.getText().toString();
     final String password = passwordText.getText().toString();
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
                      // String mail = email.toString();
                       //firebaseDatabase.getReference().child("users").child(firebaseAuth.getUid()).setValue(mail);
                   String name = userName.getText().toString().trim();
                   writeNewUser(firebaseAuth.getUid(),name,email,password);

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

    private void writeNewUser(String userId, String userName, String email,String password) {
        User user = new User(userId, userName,email,password);
        HashMap<String, Object> hashMap = user.toMap(userId, userName,email,password);
        Log.println(1,"TAG",userId);
        firebaseDatabase.getReference().child("users").child(userId).setValue(hashMap);

        //ref.child("users").child(userId).setValue(user);
    }

}
