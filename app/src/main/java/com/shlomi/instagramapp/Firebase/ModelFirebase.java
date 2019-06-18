package com.shlomi.instagramapp.Firebase;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.StringManipulation;

import java.util.HashMap;

public class ModelFirebase {

    public Context mcontext;
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

                    sendVereficationEmail();
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

    public void sendVereficationEmail(){
        FirebaseUser user = firebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(task.isSuccessful()){

                    }else{
                        Toast.makeText(mcontext,"couldnt send verefication email.",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }

    public UserSetting getUserAccoountSetting(DataSnapshot dataSnapshot){
        UserAccountSetting setting = new UserAccountSetting();
        User user = new User();
        UserSetting userSetting = new UserSetting(user,setting);

        for(DataSnapshot  ds : dataSnapshot.getChildren()){




            if(ds.getKey().equals("userAcountSetting")){

                try{
                    setting.setDisplay_name(
                            ds.child(userId).getValue(UserAccountSetting.class).getDisplay_name()

                    );
                    setting.setDescription(
                            ds.child(userId).getValue(UserAccountSetting.class).getDescription()



                    );

                    setting.setProfile_photo(
                            ds.child(userId).getValue(UserAccountSetting.class).getProfile_photo()



                    );

                    setting.setPosts(
                            ds.child(userId).getValue(UserAccountSetting.class).getPosts()



                    );

                    setting.setFollowers(
                            ds.child(userId).getValue(UserAccountSetting.class).getFollowers()



                    );
                    setting.setFollowing(
                            ds.child(userId).getValue(UserAccountSetting.class).getFollowing()

                    );
                    setting.setWebsite(
                            ds.child(userId).getValue(UserAccountSetting.class).getWebsite()



                    );
                    setting.setUserName(
                            ds.child(userId).getValue(UserAccountSetting.class).getUserName()



                    );
                }catch (NullPointerException ex){

                    Log.d("error","null pointer ex");
                }

                if(ds.getKey().equals("users")){

                    try{
                        user.setUserName(
                                ds.child(userId).getValue(User.class).getUserName()

                        );
                        user.setEmail(
                                ds.child(userId).getValue(User.class).getEmail()

                        );
                        user.setPassword(
                                ds.child(userId).getValue(User.class).getPassword()

                        );
                        user.setProfile_image_url(
                                ds.child(userId).getValue(User.class).getProfile_image_url()

                        );
                    }catch (NullPointerException ex){
                        Log.d("error","nullpointer ex from users");
                    }





                }

            }



        }
        return new UserSetting(user,setting);

    }


}
