package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DetailsPass extends AppCompatActivity {
    Intent data;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_pass);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);

        data = getIntent();

        TextView password = findViewById(R.id.passDetailsPassword);
        TextView platform = findViewById(R.id.passDetailsPlatform);
        //String notePath = data.getStringExtra("path");

       // password.setMovementMethod(new ScrollingMovementMethod());

        password.setText(data.getStringExtra("password"));
        platform.setText(data.getStringExtra("platform"));
        password.setBackgroundColor(getResources().getColor(data.getIntExtra("code", 0), null));
        FloatingActionButton fab = findViewById(R.id.editPassButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(view.getContext(), EditPass.class);
                i.putExtra("platform", data.getStringExtra("platform"));
                i.putExtra("password", data.getStringExtra("password"));
                i.putExtra("passId", data.getStringExtra("passId"));
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}