package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditPass extends AppCompatActivity {
    Intent data;
    TextView editPassPlatform, editPassPassword;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = firebaseFirestore.getInstance();

        editPassPassword = findViewById(R.id.editPassPassword);
        editPassPlatform = findViewById(R.id.editPassPlatform);

        String platform = data.getStringExtra("platform");
        String password = data.getStringExtra("password");

        editPassPlatform.setText(platform);
        editPassPassword.setText(password);
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
                editPass();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editPass() {
        String platform = editPassPlatform.getText().toString();
        String password = editPassPassword.getText().toString();

        DocumentReference docref = firebaseFirestore.collection("Passbook").document(user.getUid()).collection("myPassword").document(data.getStringExtra("passId"));

        Map<String,Object> pass = new HashMap<>();
        pass.put("platform", platform);
        pass.put("password", password);

        docref.update(pass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditPass.this, "Password Updated.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), passMain.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditPass.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                //spinner.setVisibility(View.VISIBLE);
            }
        });
    }
}