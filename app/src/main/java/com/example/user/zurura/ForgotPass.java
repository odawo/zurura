package com.example.user.zurura;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.user.zurura.R.layout.activity_forgot_pass;

public class ForgotPass extends AppCompatActivity {

    EditText change_email;
    Button send_button;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_forgot_pass);

        change_email = (EditText)findViewById(R.id.change_pwd);
        send_button = (Button)findViewById(R.id.sendbutton);

        firebaseAuth = FirebaseAuth.getInstance();

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepassword(change_email.getText().toString());
            }
        });
    }

    private void changepassword(final String email){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Snackbar snackbar = Snackbar.make(,"Check "+email, Snackbar.LENGTH_SHORT);
                    Toast.makeText(ForgotPass.this, "Check"+email,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgotPass.this, Login.class));
                }
            }
        });
    }
}
