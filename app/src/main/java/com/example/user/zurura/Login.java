package com.example.user.zurura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    Button button_login;
    EditText edittxt_email;
    EditText edit_pass;
    TextView textView_signup;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        edittxt_email = (EditText)findViewById(R.id.emailtxt);
        edit_pass = (EditText)findViewById(R.id.passwordtxt);
        textView_signup = (TextView)findViewById(R.id.textviewSignup);
        button_login = (Button)findViewById(R.id.login);

        progressDialog = new ProgressDialog(this);
        button_login.setOnClickListener(this);
        textView_signup.setOnClickListener(this);
    }

    private void userLogin()
    {
        String email = edittxt_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            //empty email and the return function stops from the function executing further
            Toast.makeText(this, "Please fill email field",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //empty password
            Toast.makeText(this, "Please fill password field",Toast.LENGTH_SHORT).show();
            return;
        }
        //progress dialog shows after validation
        progressDialog.setMessage(" Welcome " + email +" ... ");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    //ic_profile activity
                    // change the destination oncick class after the intent to the age created
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == button_login){
            userLogin();
        }
        if(view == textView_signup){
            startActivity(new Intent(this, Register_user.class));
        }
    }
}
