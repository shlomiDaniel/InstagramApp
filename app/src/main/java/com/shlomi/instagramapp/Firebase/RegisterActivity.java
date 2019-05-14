
package com.shlomi.instagramapp.Utils;
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


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.Home.Home;

import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.R;

import java.io.IOException;
import java.util.*;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText emailText;
    private EditText passwordText;
    private TextView signInTextView;
    private EditText userName;
    private ImageView profile_image;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    String url;
    FirebaseStorage storage;
    // FirebaseDatabase database;
    StorageReference storageReference;
    ModelFirebase modelFirebase;
    // FirebaseStorage storage;
    // FirebaseDatabase database;
    // StorageReference storageReference;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    // Creating StorageReference and DatabaseReference object.
    //StorageReference storageReference;
    DatabaseReference databaseReference;


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
        profile_image = (ImageView) findViewById(R.id.profile_image);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        userName = (EditText)findViewById(R.id.editUserName);
        //profile_image = (ImageView)findViewById(R.id.profile_image);
        databaseReference = firebaseDatabase.getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

//important
//        if(firebaseAuth.getCurrentUser() != null){
//            finish();
//            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
//        }
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

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
                profile_image.setImageBitmap(bitmap);
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
                    Toast.makeText(RegisterActivity.this,"Register Succses , User Created",Toast.LENGTH_SHORT).show();
                    progressDialog.cancel();

                    String name = userName.getText().toString().trim();
                    writeNewUser(firebaseAuth.getUid(),name,email,password,getUrl());
                    //checkit
                    //modelFirebase.addAcountSettingToDataBase
                    //modelFirebase.addAcountSettingToDataBase("","","",",","","");
                }else{
                    Toast.makeText(RegisterActivity.this,"Register faild , User not Created,try again.",Toast.LENGTH_SHORT).show();

                }
            }
        });

        uploadImage();

    }
    @Override
    public void onClick(View view) {
        if(view == buttonRegister){
            registerUser();
        }else if(view == signInTextView){
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
    }

    private void writeNewUser(String userId, String userName, String email,String password,String profile_image) {
        User user = new User(userId, userName,email,password,profile_image);
        HashMap<String, Object> hashMap = user.toMap(userId, userName,email,password,profile_image);
        Log.println(1,"TAG",userId);
        firebaseDatabase.getReference().child("users").child(userId).setValue(hashMap);


        //ref.child("users").child(userId).setValue(user);
    }

    private void uploadImage() {
        //String profile_image = UUID.randomUUID().toString();
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ "profile_image.png");
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Home.class));
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });


        }


    }

    public void writeImage(){

        databaseReference.child("users").child(firebaseAuth.getUid()).child("profile_image").setValue(url);
    }
    public String getUrl(){
        // String url;
        //String url;
        //final Uri uri2;
        storageReference.child("images/profile_image.png").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("profile_image : ",uri.toString());
                url = uri.toString();
                if(url != null && url != "" ){

                    writeImage();
                }


                //uri = uri;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
        return url;
    }



}
