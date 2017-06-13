package com.example.user.zurura;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by paul on 12/06/2017.
 */


public class Profile extends Fragment {

    FirebaseAuth firebaseAuth;
    TextView nameText;
    static String name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.tab1_profile, container, false);
        nameText = (TextView)rootView.findViewById(R.id.section_label);
        firebaseAuth = FirebaseAuth.getInstance();
        nameText.setText(firebaseAuth.getCurrentUser().getEmail());
        return rootView;

    }
}