package com.example.authmailgooglefb01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.authmailgooglefb01.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;

    private String userID;

    private Button logoutButton;

    //Google var
    //view binding
    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Google
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); //NOTA: binding.getRoot() ao invés de R.layout.activity_profile
        //setContentView(R.layout.activity_profile);

        //init firebase auth
        mAuth = FirebaseAuth.getInstance();
        checkUser();

        //handle click logout
        binding.BtnProfileLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                checkUser();
            }
        });
        //Google

        //MailPassword
        logoutButton = (Button) findViewById(R.id.LogoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        final TextView userGreetings = (TextView) findViewById(R.id.UserGreetings);
        final TextView userName = (TextView) findViewById(R.id.UserName);
        final TextView userEmail = (TextView) findViewById(R.id.UserEmail);

        //Buscar dados no banco
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile !=null){
                    String nome = userProfile.nome;
                    String email = userProfile.email;

                    userGreetings.setText("Olá, " + nome + "!");
                    userName.setText(nome);
                    userEmail.setText(email);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Algo errado aconteceu", Toast.LENGTH_LONG).show();

            }
        });


    }//fim onCreate

    private void checkUser() {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null){
            //user not logged in
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();

        }else{
            //user logged in
            //get user info
            String email = firebaseUser.getEmail();
            //set email
            binding.TVProfileEmail.setText(email);

        }
    }

}