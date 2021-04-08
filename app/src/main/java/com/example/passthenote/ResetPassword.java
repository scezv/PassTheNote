package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    EditText userPassword, userConfPassword;
    Button resetPasswordBtn;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        userPassword = findViewById(R.id.newPassword);
        userConfPassword = findViewById(R.id.confirmNewPassword);
        user = FirebaseAuth.getInstance().getCurrentUser();
        resetPasswordBtn = findViewById(R.id.resetPasswordBtn);

        resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userPassword.getText().toString().isEmpty()){
                    userPassword.setError("Not filled");
                    return;
                } if(userConfPassword.getText().toString().isEmpty()){
                    userConfPassword.setError("Not filled");
                    return;
                } if(!userConfPassword.getText().toString().equals(userPassword.getText().toString())){
                    userConfPassword.setError("Passwords do not match");
                    return;
                }

                user.updatePassword(userConfPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPassword.this, "Password successfully changed", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}