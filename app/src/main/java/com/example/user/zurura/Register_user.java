package com.example.user.zurura;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.firebase.auth.FirebaseUser;

public class Register_user extends AppCompatActivity {

    Button button;
    EditText edit_email;
    EditText edit_pass;
    TextView textViewlogin;
    TextView forgot_pass;

    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        button = (Button)findViewById(R.id.registerbtn);
        edit_email = (EditText)findViewById(R.id.emailtxt);
        edit_pass = (EditText)findViewById(R.id.passwordtxt);
        forgot_pass = (TextView)findViewById(R.id.textviewForgotpass);
        textViewlogin = (TextView)findViewById(R.id.textviewSignin);

        //progress dialog instance
        progressDialog = new ProgressDialog(this);

        //firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener()
        {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth)
            {
                //checks user account presence
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null)
                {
                    Intent intent = new Intent(Register_user.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };

        firebaseAuth.addAuthStateListener(authStateListener);

        //register user onclick listener
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                progressDialog.setTitle("Create Account");
                progressDialog.setMessage(" Registering user ...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                registerUser();
            }
        });

        //forgot password onclick listener
        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_user.this, ForgotPass.class));
                    finish();
            }
        });

        //login page onclick listener
        textViewlogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register_user.this, Login.class));
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private  void registerUser()
    {
        String email = edit_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(Register_user.this, "karibu", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                        startActivity(new Intent(Register_user.this, Profile.class));

                    } else{
                        Toast.makeText(Register_user.this, "Account creation error. Retry!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }
}
