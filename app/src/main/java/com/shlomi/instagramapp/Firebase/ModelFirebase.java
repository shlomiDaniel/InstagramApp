package com.shlomi.instagramapp.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.StringManipulation;

import java.util.HashMap;

public class ModelFirebase {

    public FirebaseAuth firebaseAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseStorage firebaseStorage;
    private Context context;
    public DatabaseReference databaseReference;
    public String userId;

    public ModelFirebase(Context context){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        this.context = context;
        if(firebaseAuth.getCurrentUser() != null){
            userId = firebaseAuth.getCurrentUser().getUid();
        }


    }

    public void RegisterNewEmail(final String email, String pass , String userName){
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context,"authentication Faild",Toast.LENGTH_LONG).show();
                }else if(task.isSuccessful()){
                    userId = firebaseAuth.getCurrentUser().getUid();
                    Toast.makeText(context,"Success user Createded",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public boolean checkIfUserExistOnFirebase(String userName , DataSnapshot dataSnapshot){
        User user = new User();
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Log.d("userName","check if exist" + ds);
            user.setUserName(ds.getValue(User.class).getUserName());

            if(StringManipulation.expandUserName(user.getUserName()).equals(userName)){
                return  true;
            }
        }
        return false;
    }

    public void addAcountSettingToDataBase(String email,String userName,String pass,String description, String website , String profile_photo){

//        //User user = new User(userId,userName,email,pass,profile_photo);
//        //databaseReference.child(context.getString(R.string.dbname_users)).child(userId).setValue(user);
//      //  UserAccountSetting settings = new UserAccountSetting(description,userName,
//            //    "","","",profile_photo,userName,website);
//       // databaseReference.child(context.getString(R.string.dbname_users_account_settings)).child(userId).setValue(settings);
//
//
//
//
//        UserAccountSetting settings = new UserAccountSetting("","","","","","","","");
//        HashMap<String, Object> hashMap2 = settings.toMap("","","","","","","","");
//        firebaseDatabase.getReference().child("users_acount_setings").child(userId).setValue(hashMap2);
//        //databaseReference.addAcountSettingToDataBase("","","","description","web","");
    }



}
