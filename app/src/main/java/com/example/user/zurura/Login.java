package com.example.user.zurura;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
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

public class Login extends AppCompatActivity {

    Button button_login;
    EditText edittxt_email;
    EditText edit_pass;
    TextView textView_signup;
    TextView forgot_pass;

    ProgressDialog progressDialog;
    Snackbar snackbar;

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittxt_email = (EditText)findViewById(R.id.emailtxt);
        edit_pass = (EditText)findViewById(R.id.passwordtxt);
        button_login = (Button)findViewById(R.id.login);
        forgot_pass = (TextView)findViewById(R.id.textviewForgotpass);
        textView_signup = (TextView)findViewById(R.id.textviewSignup);

        progressDialog = new ProgressDialog(this);

        //firebase instance
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //check user presence
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null)
                {
                    Intent intent = new Intent(Login.this, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);

        //onclick listeners for sign up textview and login button
        textView_signup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(Login.this, Register_user.class));
            }
        });

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgotPass.class));
                finish();
            }
        });
        button_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //progressDialog.setTitle("User login");
                progressDialog.setMessage(" logging in ");
                progressDialog.setProgressStyle(AlertDialog.THEME_HOLO_DARK);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                userLogin();
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

    private void userLogin()
    {
        String email = edittxt_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password))
        {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
            {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {

                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        Intent intent = new Intent(Login.this, HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else
                        {
                        Toast.makeText(Login.this, "Unable to login user", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        }
                }
            });
        }else
            {
                Toast.makeText(Login.this, "please fill in email and password", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

    }

}
