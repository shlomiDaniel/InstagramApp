package com.shlomi.instagramapp.Profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
//import android.widget.Toolbar;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shlomi.instagramapp.Firebase.ModelFirebase;
import com.shlomi.instagramapp.Models.Photo;
import com.shlomi.instagramapp.Models.User;
import com.shlomi.instagramapp.Models.UserAccountSetting;
import com.shlomi.instagramapp.Models.UserSetting;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.ButtonNavigationViewHelper;
import com.shlomi.instagramapp.Utils.GridImageAdapter;
import com.shlomi.instagramapp.Utils.SignInActivity;
import com.shlomi.instagramapp.Utils.UniversalImageLoader;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity  {

    private TextView userName;
    private TextView discription;
    private TextView followers;
    private TextView following;
    private TextView website;
    private TextView email;
    private TextView posts;
    private TextView name;
    private Button logOut;
    private ModelFirebase modelFirebase;
    private FirebaseAuth firebaseAuth;
    private ImageView profile_image;
    private  TextView userNameText;
    private  TextView emailtText ;
    private GridView gridView;

    private ProgressBar progressBar;
    private FirebaseDatabase mfirebasedatabase;
    private DatabaseReference myRef;
   private static  String TAG = "Profile_activity";
    private final int activity_num  = 1;
    private final int NUM_GRID_COLUMS  = 3;
    public void setupButonNavigation(){
        BottomNavigationView bn = (BottomNavigationView)findViewById(R.id.bottom_navigationViewBar);

        ButtonNavigationViewHelper.enableNavigation(ProfileActivity.this,bn);
         modelFirebase = new ModelFirebase(getApplicationContext());
        Menu menu = bn.getMenu();
        MenuItem menuItem = menu.getItem(activity_num);
        menuItem.setChecked(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
         email = (TextView)findViewById(R.id.email_id);
        setContentView(R.layout.activity_profile);
        super.onCreate(savedInstanceState);
         gridView =(GridView)findViewById(R.id.gridView);
        mfirebasedatabase =    FirebaseDatabase.getInstance();
        myRef = mfirebasedatabase.getReference();
         emailtText = (TextView)findViewById(R.id.email_id);
         userNameText = (TextView)findViewById(R.id.usernameid);
        discription = (TextView)findViewById(R.id.dicriptionid);
        website = (TextView)findViewById(R.id.websiteid);
       setupButonNavigation();
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() == null){
            finish();
            startActivity(new Intent(this, SignInActivity.class));
        }
        setupToolBar();
         setTitle("                                                ");
        FirebaseUser user = firebaseAuth.getCurrentUser();
       initImageLoader();

       tempGridSetup();
       setupGridView();
        Button editProfile = (Button)findViewById(R.id.editProfileButtonid);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick","navigation to EditProfile");
                Intent intent = new Intent(ProfileActivity.this
                        ,accountSettingsActivity.class);
                intent.putExtra(getString
                        (R.string.calling_activity),
                        getString(R.string.profile_activity));
                       startActivity(intent);
            }
        });



       myRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               setProfileWidget( modelFirebase.getUserAccoountSetting(dataSnapshot));
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });
    }

    private void setupGridView(){
        final ArrayList<Photo>photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference.child("user_photos").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              for(DataSnapshot singleSnapShot : dataSnapshot.getChildren()){
                  photos.add(singleSnapShot.getValue(Photo.class));
              }
              int gridWidth = getResources().getDisplayMetrics().widthPixels;
              int imgWidth = gridWidth/NUM_GRID_COLUMS;
              gridView.setColumnWidth(imgWidth);
              ArrayList<String>imgUrls = new ArrayList<>();
              for(int i=0;i<photos.size();i++){
                  imgUrls.add(photos.get(i).getImage_path());
              }
              //check this row
              GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this,R.layout.layaout_grid_imgview,"",imgUrls);
              gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

    private void setProfileWidget(UserSetting userSetting){
        User user = userSetting.getUser();

        UserAccountSetting userAccountSetting = userSetting.getUserAccountSetting();
//        UniversalImageLoader.setIamge(userAccountSetting.getProfile_photo()
//                ,profile_image,null,"");
        //FirebaseUser user = firebaseAuth.getCurrentUser();

        emailtText.setText(user.getEmail());

         userNameText.setText(user.getUserName());
        //email.setText("a");
        //email.setText("aaaaaaa");
        //userName.setText(user.getUserName());
        website.setText(userAccountSetting.getWebsite());
        discription.setText(userAccountSetting.getDescription());
//        followers.setText(String.valueOf(userAccountSetting.getFollowers()));
//        following.setText(String.valueOf(userAccountSetting.getFollowing()));
//        posts.setText(String.valueOf(userAccountSetting.getPosts()));




    }
    private void setProfile_image(){


        UniversalImageLoader.setIamge("http://www.teknoustam.com/wp-content/uploads/2018/06/android.png"
                ,profile_image,null,"");
    }

    private void setupActivityWidgets(){
//        progressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
//        progressBar.setVisibility(View.GONE);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        profile_image = (ImageView) findViewById(R.id.profile_image);
       // TextView userNameText = (TextView)findViewById(R.id.usernameid);
       // userNameText.setText(user.getDisplayName());

        profile_image = (ImageView) findViewById(R.id.profile_image);
        TextView emailtText = (TextView)findViewById(R.id.email_id);
        emailtText.setText(user.getEmail());


    }

    public void setupImageGreedView(ArrayList<String>imgUrls){
        GridView gridView = findViewById(R.id.gridView);
        int gridwidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth = gridwidth/3;
        gridView.setColumnWidth(imgWidth);
        GridImageAdapter adapter = new GridImageAdapter(ProfileActivity.this,R.layout.layaout_grid_imgview,"",imgUrls);
        gridView.setAdapter(adapter);
    }

    private  void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(ProfileActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }

    private void setupToolBar(){

        Toolbar toolbar = (Toolbar)findViewById(R.id.profileToolBar);
        setSupportActionBar(toolbar);

        ImageView profile_image_view = (ImageView)findViewById(R.id.profile_menu);
        profile_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"ONCLICK NAVIGATION ACOUNT SETTING");
                Intent intent = new Intent(ProfileActivity.this,accountSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void tempGridSetup(){
        ArrayList<String> imgUrls = new ArrayList<>();
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        imgUrls.add("https://previews.123rf.com/images/underworld/underworld1402/underworld140200575/25668343-%E9%9D%9E%E5%B8%B8%E3%81%AB%E9%95%B7%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%82%92%E6%8C%81%E3%81%A4%E7%8F%8D%E3%81%97%E3%81%84%E3%83%94%E3%83%B3%E3%82%AF%E3%81%AE%E3%82%AA%E3%82%A6%E3%83%A0%E9%B3%A5.jpg");
        imgUrls.add("https://previews.123rf.com/images/sergioboccardo/sergioboccardo1503/sergioboccardo150300132/38731688-%E3%83%96%E3%83%A9%E3%82%B8%E3%83%AB%E3%81%AE%E9%BB%84%E8%89%B2%E7%9B%AE%E9%BB%92%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%81%A8%E3%83%95%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B4.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/8/8e/American_Flamingo_-_Phoenicopterus_ruber.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        imgUrls.add("https://previews.123rf.com/images/underworld/underworld1402/underworld140200575/25668343-%E9%9D%9E%E5%B8%B8%E3%81%AB%E9%95%B7%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%82%92%E6%8C%81%E3%81%A4%E7%8F%8D%E3%81%97%E3%81%84%E3%83%94%E3%83%B3%E3%82%AF%E3%81%AE%E3%82%AA%E3%82%A6%E3%83%A0%E9%B3%A5.jpg");
        imgUrls.add("https://previews.123rf.com/images/sergioboccardo/sergioboccardo1503/sergioboccardo150300132/38731688-%E3%83%96%E3%83%A9%E3%82%B8%E3%83%AB%E3%81%AE%E9%BB%84%E8%89%B2%E7%9B%AE%E9%BB%92%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%81%A8%E3%83%95%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B4.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/8/8e/American_Flamingo_-_Phoenicopterus_ruber.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/f/f9/Phoenicopterus_ruber_in_S%C3%A3o_Paulo_Zoo.jpg");
        imgUrls.add("https://previews.123rf.com/images/underworld/underworld1402/underworld140200575/25668343-%E9%9D%9E%E5%B8%B8%E3%81%AB%E9%95%B7%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%82%92%E6%8C%81%E3%81%A4%E7%8F%8D%E3%81%97%E3%81%84%E3%83%94%E3%83%B3%E3%82%AF%E3%81%AE%E3%82%AA%E3%82%A6%E3%83%A0%E9%B3%A5.jpg");
        imgUrls.add("https://previews.123rf.com/images/sergioboccardo/sergioboccardo1503/sergioboccardo150300132/38731688-%E3%83%96%E3%83%A9%E3%82%B8%E3%83%AB%E3%81%AE%E9%BB%84%E8%89%B2%E7%9B%AE%E9%BB%92%E3%81%84%E3%81%8F%E3%81%A1%E3%81%B0%E3%81%97%E3%81%A8%E3%83%95%E3%83%A9%E3%83%9F%E3%83%B3%E3%82%B4.jpg");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/8/8e/American_Flamingo_-_Phoenicopterus_ruber.jpg");

        setupImageGreedView(imgUrls);


    }


}
