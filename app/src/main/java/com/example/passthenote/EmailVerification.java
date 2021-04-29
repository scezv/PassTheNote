package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailVerification extends AppCompatActivity {
    Button logoutBtn, verifyEmail, loginBtn;
    TextView verifyMsg;
    FirebaseAuth firebaseAuth;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firebaseAuth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logoutBtn);
        verifyMsg = findViewById(R.id.verifyMsg);
        verifyEmail = findViewById(R.id.verifyEmail);
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        loginBtn = findViewById(R.id.chooseBtnEmail);

        //check if email is verified or not
        if (firebaseAuth.getCurrentUser().isEmailVerified()) {
            verifyEmail.setVisibility(View.INVISIBLE);
            verifyMsg.setVisibility(View.INVISIBLE);
        } else {
            verifyEmail.setVisibility(View.VISIBLE);
            verifyMsg.setVisibility(View.VISIBLE);
        }
        // verifies e-mail
        verifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EmailVerification.this, "Verification Email Sent", Toast.LENGTH_SHORT).show();
                        verifyMsg.setText("Check your inbox/spam to verify your e-mail and login again!");
                        verifyEmail.setVisibility(View.INVISIBLE);
                        loginBtn.setText("Login");
                        loginBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        //logouts
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseAuth.getCurrentUser().isEmailVerified()){
            startActivity(new Intent(getApplicationContext(), ChooseOption.class));
            finish();
        }
    }
    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.option_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    // menu selection on three dots
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        // takes to password update password
//        if(item.getItemId() == R.id.resetPassword){
//            startActivity(new Intent(getApplicationContext(), ResetPassword.class));
//            finish();
//        }
//        // updates e-mail on the record
//        if(item.getItemId() == R.id.updateEmailMenu){
//            View view = inflater.inflate(R.layout.update_email, null);
//            reset_alert.setTitle("Update E-mail Address?")
//                    .setMessage("Enter the E-mail Address you want to change to")
//                    .setPositiveButton("Update", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // validate email of the user
//                            EditText email = view.findViewById(R.id.updateEmail);
//                            if(email.getText().toString().isEmpty()){
//                                email.setError("Required Field");
//                                return;
//                            }
//                            // validate password of the user
//                            EditText password = view.findViewById(R.id.updateEmailPass);
//
//                            String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
//
//                            firebaseAuth.signInWithEmailAndPassword(userEmail, password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
//                                @Override
//                                public void onSuccess(AuthResult authResult) {
//                                    firebaseAuth.getCurrentUser().updateEmail(email.getText().toString());
//                                    Toast.makeText(EmailVerification.this, "E-mail Updated", Toast.LENGTH_LONG).show();
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(EmailVerification.this, "Password Incorrect", Toast.LENGTH_LONG).show();
//                                }
//                            });
//                        }
//                    }).setNegativeButton("Cancel", null)
//                    .setView(view)
//                    .create().show();
//        }
//        if(item.getItemId() == R.id.logoutAccount){
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(getApplicationContext(), Login.class));
//            finish();
//        }
//        return super.onOptionsItemSelected(item);
//    }
}