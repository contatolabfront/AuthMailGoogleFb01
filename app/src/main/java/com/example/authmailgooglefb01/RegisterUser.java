package com.example.authmailgooglefb01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    private TextView registerBanner;
    private EditText registerName, registerEmail, registerPassword;
    private ProgressBar registerProgressBar;
    private Button registerButton;

    //todo conferir se email já foi cadastrado antes
    //todo verificar email após registrar com sucesso
    //todo voltar para tela inicial após verificação

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerBanner = (TextView) findViewById(R.id.RegisterBanner);
        registerBanner.setOnClickListener(this);

        registerButton = (Button) findViewById(R.id.Registerbutton);
        registerButton.setOnClickListener(this);

        registerName = (EditText) findViewById(R.id.RegisterName);
        registerEmail = (EditText) findViewById(R.id.RegisterEmail);
        registerPassword = (EditText) findViewById(R.id.RegisterPassword);

        registerProgressBar = (ProgressBar) findViewById(R.id.RegisterProgressBar);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.RegisterBanner:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.Registerbutton:
                registerUser();
                break;
        }

    }

    private void registerUser() {
        String name = registerName.getText().toString().trim();
        String email = registerEmail.getText().toString().trim();
        String password = registerPassword.getText().toString().trim();

        if (name.isEmpty()){
            registerName.setError("Nome completo é necessário");
            registerName.requestFocus();
            return;
        }

        if (email.isEmpty()){
            registerEmail.setError("Email é necessário");
            registerEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            registerEmail.setError("Insira um email válido");
            registerEmail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            registerPassword.setError("Senha é necessária");
            registerPassword.requestFocus();
            return;
        }

        if (password.length()<6){
            registerPassword.setError("Senha deve ter mais de 6 caracteres");
            registerPassword.requestFocus();
        }

        registerProgressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            User user = new User(name,email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterUser.this, "Usuário registrado com sucesso", Toast.LENGTH_LONG).show();
                                        registerProgressBar.setVisibility(View.GONE);

                                        //redirect to Login Layout
                                    }
                                    else{
                                        Toast.makeText(RegisterUser.this, "Falhou ao registrar, tente novamente", Toast.LENGTH_LONG).show();
                                        registerProgressBar.setVisibility(View.GONE);
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(RegisterUser.this, "Falhou ao registrar", Toast.LENGTH_LONG).show();
                            registerProgressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}