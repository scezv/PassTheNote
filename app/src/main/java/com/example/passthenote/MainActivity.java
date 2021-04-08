package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button logoutBtn, verifyEmail;
    TextView verifyMsg;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logoutBtn);
        verifyMsg = findViewById(R.id.verifyMsg);
        verifyEmail = findViewById(R.id.verifyEmail);
        System.out.println(firebaseAuth.getCurrentUser().isEmailVerified());

        if(firebaseAuth.getCurrentUser().isEmailVerified() == false){
            verifyEmail.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }

        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        verifyEmail.setVisibility(View.INVISIBLE);
                        verifyMsg.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.resetPassword){
            startActivity(new Intent(getApplicationContext(), ResetPassword.class));
        }
        return super.onOptionsItemSelected(item);
    }
}