package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChooseOption extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        Button notes = findViewById(R.id.notesButton);
        Button pass = findViewById(R.id.passwordButton);

        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), notesMain.class));
            }
        });

        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!user.isAnonymous()){
                    View view = inflater.inflate(R.layout.pass_check,null);
                    reset_alert.setTitle("Password Verification")
                            .setMessage("Enter your PassTheNote's password to view your saved passwords")
                            .setPositiveButton("Check", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    EditText pass = view.findViewById(R.id.passCheck);

                                    firebaseAuth.signInWithEmailAndPassword(firebaseAuth.getCurrentUser().getEmail(), pass.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(ChooseOption.this, "Going to your passwords", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getApplicationContext(), passMain.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(ChooseOption.this, "Current Password Incorrect", Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }).setNegativeButton("Cancel", null)
                            .setView(view)
                            .create().show();
                } else {
                    startActivity(new Intent(getApplicationContext(), passMain.class));
                    finish();
                }
            }
        });
    }
}