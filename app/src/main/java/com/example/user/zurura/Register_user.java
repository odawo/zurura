package com.example.user.zurura;

import android.app.ProgressDialog;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register_user extends AppCompatActivity implements View.OnClickListener {

    Button button;
    EditText edit_name;
    EditText edit_email;
    EditText edit_pass;
    TextView textView;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        button = (Button)findViewById(R.id.registerbtn);
        edit_name = (EditText) findViewById(R.id.username);
        edit_email = (EditText)findViewById(R.id.emailtxt);
        edit_pass = (EditText)findViewById(R.id.passwordtxt);
        textView = (TextView)findViewById(R.id.textviewSignin);

        button.setOnClickListener(this);
        textView.setOnClickListener(this);
    }

    private  void registerUser()
    {
        String user_name = edit_name.getText().toString().trim();
        String  email = edit_email.getText().toString().trim();
        String password = edit_pass.getText().toString().trim();

        if(TextUtils.isEmpty(user_name)){
            Toast.makeText(this, "Please fill in your username", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(email)) {
            //empty email and stops from the function executing further
            Toast.makeText(this, "Please fill email field",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            //empty password
            Toast.makeText(this, "Please fill password field",Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage(" Registering " + user_name +" ... ");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {//user is registered and profile activity begins
                    Toast.makeText(Register_user.this, "welcome", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Register_user.this, "Unable to register. Retry", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if(view == button){
            registerUser();
        }
        if(view == textView){
            //opens login activity
        }
    }
}
