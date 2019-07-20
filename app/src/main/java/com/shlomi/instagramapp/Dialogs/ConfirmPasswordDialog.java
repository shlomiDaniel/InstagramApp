package com.shlomi.instagramapp.Dialogs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.shlomi.instagramapp.Profile.ProfileActivity;
import com.shlomi.instagramapp.R;

import org.w3c.dom.Text;

public class ConfirmPasswordDialog extends DialogFragment {
    public interface  OnconfirmPasswordListner{

         void onConfirmPass(String pass);
    }
    TextView mPass;
    OnconfirmPasswordListner onconfirmPasswordListner;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_pass,container,false);
        TextView cancelDialog = (TextView)view.findViewById(R.id.daialogCancel);

        TextView confirm = (TextView)view.findViewById(R.id.dialogConfirm);
        mPass = (TextView)view.findViewById(R.id.confirmPass);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String password = mPass.getText().toString();
               // onconfirmPasswordListner.onConfirmPass(password);
                if(!password.equals("")){
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
                    startActivity(intent);
                    onconfirmPasswordListner.onConfirmPass(password);

                }else{
                    Toast.makeText(getActivity(),"you must enter a vaild pass",Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{

            onconfirmPasswordListner = (OnconfirmPasswordListner)getTargetFragment();
        }catch (ClassCastException e){

        }
    }
}
