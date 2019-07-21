package com.shlomi.instagramapp.Profile;

import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Dialogs.ConfirmPasswordDialog;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.Share.ShareActivity;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;
import com.shlomi.instagramapp.R;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnconfirmPasswordListner {

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
private ImageView profilePhoto;

private FirebaseAuth mAuth;
private FirebaseAuth.AuthStateListener authStateListener;
private FirebaseDatabase firebaseDatabase;
private ModelFirebase modelFirebase;
private EditText userName,displayName,website,description,email;
private TextView changeProfilePhoto;
private CircleImageView mProfilePhoto;
private String userid;
private FirebaseStorage storage;
    // FirebaseDatabase database;
  private   StorageReference storageReference;
private UserSetting mUserSettings;
private ImageView saveChanges;
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile,container,false);
         mAuth = FirebaseAuth.getInstance();
        profilePhoto = (ImageView)view.findViewById(R.id.profile_photo_id);
        displayName = (EditText) view.findViewById(R.id.display_name);
        userName = (EditText) view.findViewById(R.id.user_name);
        website = (EditText) view.findViewById(R.id.website);
        description = (EditText) view.findViewById(R.id.description);
        email = (EditText) view.findViewById(R.id.email);
        changeProfilePhoto = (TextView) view.findViewById(R.id.changeProfilePhoto);
        modelFirebase = new ModelFirebase(getContext());
        saveChanges = (ImageView)view.findViewById(R.id.save_changes);
        // profilePhoto.setimage
        // initImageLoader();
        //setProfileImage();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
         storage = FirebaseStorage.getInstance();
         storageRef = storage.getReference();
        //storage = FirebaseStorage.getInstance();
        StorageReference photoReference = storageRef.child("photos").child("users").child(mAuth.getCurrentUser().getUid()).child("profile_image");
        photoReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
             profilePhoto.setImageURI(uri);
                Glide.with(getContext() /* context */).load(uri).into(profilePhoto);
            }
        });



        ImageView backArrow = (ImageView)view.findViewById(R.id.profile_menu);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();

            }
        });




//        changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ShareActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getActivity().startActivity(intent);
//            }
//        });

        setupFirebase();

       saveChanges.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               saveProfileSettings();
               Toast.makeText(getActivity(),"here",Toast.LENGTH_SHORT).show();




           }
       });

        return view;


    }

    public void changeUserName(String user_name){

           // firebaseDatabase.getReference().child("users").child(userid).setValue("aaa");

      String s = userName.getText().toString();
      firebaseDatabase.getReference().setValue(s);


        //firebaseDatabase.getReference().add

    }

    private void saveProfileSettings(){
        final String displayNamee = displayName.getText().toString();
        final String user_name = userName.getText().toString();
        final String web = website.getText().toString();
        final String desc = description.getText().toString();
        final String emaill = email.getText().toString();



                if(!mUserSettings.getUser().getUserName().equals(user_name)){
                     // checkIfUserNameExist(user_name);
                }
                if(!mUserSettings.getUser().getEmail().equals(emaill)){
                    ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
                    dialog.show(getFragmentManager(),getString(R.string.confirm_passwoed_dialog));
                    dialog.setTargetFragment(EditProfileFragment.this,1);


                }


        if(!mUserSettings.getUserAccountSetting().getDisplay_name().equals(displayNamee)){
            modelFirebase.updateUserAcountSetting(displayNamee,null,null,null);
        }
        if(!mUserSettings.getUserAccountSetting().getWebsite().equals(web)){
            modelFirebase.updateUserAcountSetting(null,null,null,web);

        }
        if(!mUserSettings.getUserAccountSetting().getDescription().equals(desc)){
            modelFirebase.updateUserAcountSetting(null,null,desc,null);

        }
        if(!mUserSettings.getUserAccountSetting().getUserName().equals(user_name)){
            modelFirebase.updateUserAcountSetting(null,user_name,null,null);

        }






    }

    public void checkIfUserNameExist(final String user_name) {
               DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
               Query query = reference.child("users").
                        orderByChild(getString(R.string.filed_user_name)).equalTo(user_name);
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if(!dataSnapshot.exists()){
                            modelFirebase.updateUserName(user_name);
                            Toast.makeText(getActivity(),"saved user name"
                                   ,Toast.LENGTH_SHORT).show();
                       }
                          for(DataSnapshot ds : dataSnapshot.getChildren()){

                             if(ds.exists()){
                                 Toast.makeText(getActivity(),"The user name already exist."
                                         ,Toast.LENGTH_SHORT).show();
                             }
                            }

                 }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void setupFirebase(){
        mAuth = FirebaseAuth.getInstance();
        userid = mAuth.getCurrentUser().getUid();
        firebaseDatabase = FirebaseDatabase.getInstance();
       firebaseDatabase.getReference().addValueEventListener(new ValueEventListener() {

           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             setProfileWidget(modelFirebase.getUserAccoountSetting(dataSnapshot));
             //checkIfUserNameExist(userName);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });





    }




    private void setProfileWidget(final UserSetting userSetting){

        mUserSettings = userSetting;

        User user = userSetting.getUser();

        UserAccountSetting userAccountSetting = userSetting.getUserAccountSetting();
        UniversalImageLoader.setIamge(userAccountSetting.getProfile_photo()
                ,profilePhoto,null,"");

        email.setText(user.getEmail());
        displayName.setText(userAccountSetting.getDisplay_name());
        userName.setText(userAccountSetting.getUserName());

        website.setText(userAccountSetting.getWebsite());
        description.setText(userAccountSetting.getDescription());
        changeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);

            }
        });

    }


    private void setProfileImage(){
        Log.d("Edit Profile Fragment","setting profile image");
        String imageUrl = "http://www.teknoustam.com/wp-content/uploads/2018/06/android.png";
        UniversalImageLoader.setIamge(imageUrl,profilePhoto,null,"");
        uploadFileFromDataMemory();
    }

    public void  uploadFileFromDataMemory(){
     StorageReference ref = storageReference.child("images/"+ "profile_image.png");
        //String userId = firebaseAuth.getCurrentUser().getUid();
        ref =   storageReference.child("photos").child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profile_image.png");
        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {

            }
        });



        profilePhoto.setDrawingCacheEnabled(true);
        profilePhoto.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }



    @Override
    public void onConfirmPass(String pass) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

             // Get auth credentials from the user for re-authentication. The example below shows
            // email and password credentials but there are multiple possible providers,
            // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), pass);

         // Prompt the user to re-provide their sign-in credentials
        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "User re-authenticated.");
                            mAuth.fetchProvidersForEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                 if(task.isSuccessful()){
                                     try{

                                         if(task.getResult().getProviders().size()==1){
                                             Toast.makeText(getActivity(),"that email alreay in use",Toast.LENGTH_SHORT).show();
                                         }else {
                                             // Log.d(this,"that email is avalivble");

                                             mAuth.getCurrentUser().updateEmail(email.getText().toString())
                                                     .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                         @Override
                                                         public void onComplete(@NonNull Task<Void> task) {
                                                             if (task.isSuccessful()) {
                                                                 Log.d(TAG, "User email address updated.");
                                                                 Toast.makeText(getActivity(),"email updated.",Toast.LENGTH_SHORT).show();
                                                                 Toast.makeText(getActivity(),email.getText().toString(),Toast.LENGTH_SHORT).show();
                                                                 modelFirebase.updateEmail(email.getText().toString());
                                                             }
                                                         }
                                                     });

                                     }



                                     }catch (NullPointerException e){

                                     }
                                 }else{

                                 }
                                }
                            });

                        }else{
                            Log.d(TAG, "User re-authenticated faild.");

                        }
                    }
                });



    }

//    private  void initImageLoader(){
//        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
//        ImageLoader.getInstance().init(universalImageLoader.getConfig());
//    }




}


