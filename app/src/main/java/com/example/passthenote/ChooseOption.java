package com.example.passthenote;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseOption extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_option);

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
                Toast.makeText(ChooseOption.this,"PASS Clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}