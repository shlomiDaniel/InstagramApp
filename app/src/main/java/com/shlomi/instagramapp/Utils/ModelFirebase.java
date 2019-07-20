package com.shlomi.instagramapp.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class ModelFirebase {



        public static DatabaseReference ref;

       public ModelFirebase() {
            ref = FirebaseDatabase.getInstance().getReference();

        }



}
