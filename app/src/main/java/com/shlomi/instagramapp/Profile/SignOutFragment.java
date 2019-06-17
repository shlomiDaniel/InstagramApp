package com.shlomi.instagramapp.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shlomi.instagramapp.Home.Home;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.MainActivity;

import org.w3c.dom.Text;

public class SignOutFragment extends Fragment  {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListner;

    private ProgressBar mProgressBar;
    private TextView textViewtSingOut,textSignOut;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out,container,false);

        textViewtSingOut = (TextView)view.findViewById(R.id.tvConfirmSignOut);
        mProgressBar = (ProgressBar)view.findViewById(R.id.progressBar);
        textSignOut = (TextView)view.findViewById(R.id.textSignOut);
        final Button signOut = (Button)view.findViewById(R.id.sign_out_button);
        mProgressBar.setVisibility(View.GONE);
        //textViewtSingOut.setVisibility(View.GONE);
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                signOutClicked();
            }
        });
        return view;
    }

public void signOutClicked(){

if(FirebaseAuth.getInstance().getCurrentUser()!= null)

    //Toast.makeText(this,"logingOutSuccess", "logedOut",Toast.LENGTH_SHORT).show();
    Toast.makeText(getActivity(),"logedOut Succses , User logedout",Toast.LENGTH_SHORT).show();
    FirebaseAuth.getInstance().signOut();
    mProgressBar.setVisibility(View.VISIBLE);
    textSignOut.setVisibility(View.VISIBLE);

    Intent intent = new Intent(getActivity(), MainActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    startActivity(intent);

}



}

