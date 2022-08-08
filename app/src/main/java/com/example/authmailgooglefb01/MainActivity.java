package com.example.authmailgooglefb01;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

//Facebook
import com.example.authmailgooglefb01.databinding.ActivityMainBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

//Google
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Picasso
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //MailPassword Variables
    private TextView register, forgotPassword;
    private EditText loginEmail,loginPassword;
    private Button mailPasswordloginButton, iBGoogleAuth,iBFacebookAuth; //era loginButton
    private ProgressBar progressBar;

    //Firebase var
    private CallbackManager callbackManager;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NOTA! mudou //setContentView(R.layout.activity_main); para setContentView(binding.getRoot());

        setContentView(R.layout.activity_main);

        //MailPassword

        //init Firebase auth

        mFirebaseAuth = FirebaseAuth.getInstance();


        //mAuth = FirebaseAuth.getInstance(); // não adotou nome firebaseAuth do tuto de Atif Pervaiz

        register = (TextView) findViewById(R.id.registerTextView);
        register.setOnClickListener(this);

        forgotPassword = (TextView) findViewById(R.id.ForgotPassword);
        forgotPassword.setOnClickListener(this);

        mailPasswordloginButton = (Button) findViewById(R.id.MailPasswordLoginButton);
        mailPasswordloginButton.setOnClickListener(this);

        loginEmail = (EditText) findViewById(R.id.LoginEmail);
        loginPassword = (EditText) findViewById(R.id.LoginPassword);

        iBGoogleAuth = (Button) findViewById(R.id.BTNGoogleAuth);
        iBGoogleAuth.setOnClickListener(this);

        iBFacebookAuth = (Button) findViewById(R.id.BTNFacebookAuth);
        iBFacebookAuth.setOnClickListener(this);

        progressBar = (ProgressBar) findViewById(R.id.LoginProgressBar);

    }

    //MailPassword------------------------------

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerTextView:
                startActivity(new Intent(this, RegisterUser.class));
                break;
            case R.id.MailPasswordLoginButton:
                userLogin();
                break;
            case R.id.ForgotPassword:
                startActivity(new Intent(MainActivity.this,ForgotPassword.class));
                break;
            case R.id.BTNGoogleAuth:
                startActivity(new Intent (MainActivity.this,GoogleAuth.class));
                break;
            case R.id.BTNFacebookAuth:
                startActivity(new Intent(MainActivity.this,FacebookAuth.class));
                break;
        }
    }

    private void userLogin() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()){
            loginEmail.setError("Email é necessário");
            loginEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            loginEmail.setError("Insira um email válido");
            loginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            loginPassword.setError("Senha é necessária");
            loginPassword.requestFocus();
            return;
        }

        if (password.length()<6){
            loginPassword.setError("Senha deve ter mais de 6 caracteres");
            loginPassword.requestFocus();
        }

        progressBar.setVisibility(View.VISIBLE);

        mFirebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    //checa se email foi verificado
                    //Todo: passar para a atividade de registro logo após clicar botão de registrar
                    if(user.isEmailVerified()){

                        //redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));

                    }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Cheque o email para verificar a conta", Toast.LENGTH_LONG).show();
                    }


                }else{
                    Toast.makeText(MainActivity.this, "Falhou ao logar, cheque seus dados", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //MailPassword------------------------------



}