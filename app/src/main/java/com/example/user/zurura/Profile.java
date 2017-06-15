package com.example.user.zurura;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by paul on 12/06/2017.
 */


public class Profile extends Fragment {

    FirebaseAuth firebaseAuth;
    TextView nameText;
    static String name;

    ImageButton imageButton_edit;
    ImageButton save_btn;
    TextView username_textView;
    TextView email_textView;
    Button password_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.tab1_profile, container, false);
        nameText = (TextView)rootView.findViewById(R.id.user_useremail);
        imageButton_edit = (ImageButton)rootView.findViewById(R.id.menu_edit);
        firebaseAuth = FirebaseAuth.getInstance();
        nameText.setText(firebaseAuth.getCurrentUser().getEmail());

        email_textView = (TextView)rootView.findViewById(R.id.user_username);
        username_textView = (TextView)rootView.findViewById(R.id.user_useremail);
        password_btn = (Button) rootView.findViewById(R.id.user_userpasswordbtn);
        save_btn = (ImageButton) rootView.findViewById(R.id.user_usersavebtn);

        if(imageButton_edit.performClick()){
            imageButton_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    email_textView.setClickable(true);
                    username_textView.setClickable(true);
                    password_btn.setClickable(true);
                    save_btn.setVisibility(View.VISIBLE);

                    save_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String username = username_textView.getText().toString().trim();
                            final String email = email_textView.getText().toString().trim();
                            final String password = password_btn.getText().toString().trim();

                            final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            AuthCredential authCredential = EmailAuthProvider.getCredential(email,password);
                            //user prompted to re-provide their login credentials
                            firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        firebaseUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Log.d(email,"email updated");
                                                }else {
                                                    Log.d(email,"error email not updated. Retry.");
                                                }
                                            }
                                        });
                                    }else {
                                        Log.d(email,"Error authentication failure");
                                    }
                                }

                            });


                            password_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                    AuthCredential authCredential = EmailAuthProvider.getCredential(email,password);
                                    //user prompted to re-provide their login credentials
                                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                firebaseUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Log.d(password,"password changeded");
                                                        }else {
                                                            Log.d(password,"error password not changed. Retry.");
                                                        }
                                                    }
                                                });
                                            } else {
                                                Log.d(password, "Error authentication failure");
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    });
                }

            });
        }
        else {
            email_textView.setClickable(false);
            username_textView.setClickable(false);
            password_btn.setClickable(false);
            save_btn.setVisibility(View.INVISIBLE);
        }

        return rootView;

    }
}