package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewPass extends AppCompatActivity {
    private EditText passPlatform, passPassword;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        passPlatform = findViewById(R.id.add_pass_platform);
        passPassword = findViewById(R.id.add_pass_password);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                saveNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void saveNote() {
        String platform = passPlatform.getText().toString();
        String password = passPassword.getText().toString();

        if (platform.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a platform name and password", Toast.LENGTH_LONG).show();
            return;
        }
        DocumentReference docref = FirebaseFirestore.getInstance().collection("Passbook").document(user.getUid()).collection("myPassword").document();
        Map<String, Object> note = new HashMap<>();
        note.put("platform", platform);
        note.put("password", password);

        docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewPass.this, "Password Saved", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), passMain.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewPass.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                //progressBarSave.setVisibility(View.VISIBLE);
            }
        });
    }
}