package com.example.authmailgooglefb01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText emailEditTextForgot;
    private Button resetPasswordButton;
    private ProgressBar progressBarForgot;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailEditTextForgot = (EditText) findViewById(R.id.EmailEditTextForgot);
        resetPasswordButton = (Button) findViewById(R.id.ResetPasswordButton);
        progressBarForgot = (ProgressBar) findViewById(R.id.ProgressBarForgot);

        auth = FirebaseAuth.getInstance();

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = emailEditTextForgot.getText().toString().trim();

        if(email.isEmpty()){
            emailEditTextForgot.setError("Email é necessário");
            emailEditTextForgot.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditTextForgot.setError("Insira um email válido");
            emailEditTextForgot.requestFocus();
            return;
        }
        progressBarForgot.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Confira seu email para redefinir a senha",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ForgotPassword.this,"Tente novamente, algo errado aconteceu",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}