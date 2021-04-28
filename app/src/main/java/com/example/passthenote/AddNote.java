package com.example.passthenote;

import android.os.Bundle;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

public class AddNote extends AppCompatActivity {
    FirebaseFirestore fireStore;
    TextView noteTitle, noteContent;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fireStore = FirebaseFirestore.getInstance();
        noteTitle =  findViewById(R.id.addNoteTitle);
        noteContent = findViewById(R.id.addNoteContent);
        progressBar = findViewById(R.id.progressBar);


        FloatingActionButton fab = findViewById(R.id.addNoteIcon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nTitle = noteTitle.getText().toString();
                String nContent = noteContent.getText().toString();

                // checks if title or content is empty or not
                if(nTitle.isEmpty() || nContent.isEmpty()){
                    Toast.makeText(AddNote.this, "Required field is empty, cannot create the note", Toast.LENGTH_LONG).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                // saves note to firebase
                DocumentReference docRef = fireStore.collection("notes").document();
                Map<String, Object> note = new HashMap<>();
                note.put("title", nTitle);
                note.put("content", nContent);

                docRef.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AddNote.this,"note added",Toast.LENGTH_SHORT).show();
                        onBackPressed();
                        Toast.makeText(AddNote.this,"note added",Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddNote.this,"Error, note not added!!!",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.close){
            Toast.makeText(AddNote.this,"Note is not saved",Toast.LENGTH_SHORT).show();
            onBackPressed();
            Toast.makeText(AddNote.this,"Note is not saved",Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}