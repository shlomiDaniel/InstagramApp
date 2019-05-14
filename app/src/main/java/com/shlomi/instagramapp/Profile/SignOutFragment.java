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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.shlomi.instagramapp.Home.Home;
import com.shlomi.instagramapp.R;
import com.shlomi.instagramapp.Utils.MainActivity;

public class SignOutFragment extends Fragment  {

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_out,container,false);

        final Button signOut = (Button)view.findViewById(R.id.sign_out_button);
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
    Intent intent = new Intent(getActivity(), MainActivity.class);
    startActivity(intent);

}


}

