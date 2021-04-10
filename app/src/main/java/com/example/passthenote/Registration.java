package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    EditText registerName, registerEmailAddress, registerPassword, registerConfirmPassword;
    Button registerButton, returnToLogin;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        registerName = findViewById(R.id.registerName);
        registerEmailAddress = findViewById(R.id.registerEmailAddress);
        registerPassword = findViewById(R.id.registerPassword);
        registerConfirmPassword = findViewById(R.id.registerConfirmPassword);
        returnToLogin = findViewById(R.id.returnToLogin);
        registerButton = findViewById(R.id.registerButton);
        fAuth = FirebaseAuth.getInstance();

        // takes back to login page
        returnToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
        // click listener on register button
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = registerName.getText().toString();
                String EmailAddress = registerEmailAddress.getText().toString();
                String Password = registerPassword.getText().toString();
                String ConfirmPassword = registerConfirmPassword.getText().toString();

                if(Name.isEmpty()){
                    registerName.setError("Enter something here dummy!! pffh");
                    return;
                }
                if(EmailAddress.isEmpty()){
                    registerEmailAddress.setError("Enter a valid E-mail address");
                    return;
                }
                if(Password.isEmpty()){
                    registerPassword.setError("Enter a password");
                    return;
                }
                if(ConfirmPassword.isEmpty()){
                    registerConfirmPassword.setError("Enter the above password, again");
                    return;
                }
                if(!Password.equals(ConfirmPassword)){
                    registerConfirmPassword.setError("The password you entered doesn't match");
                    return;
                }
                Toast.makeText(Registration.this, "Account Successfully Created", Toast.LENGTH_LONG).show();
                fAuth.createUserWithEmailAndPassword(EmailAddress, Password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // send user to dashboard
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Registration.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}