package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.passthenote.model.NoteTransfer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNote extends AppCompatActivity {
    Intent data;
    TextView editNoteTitle, editNoteDescription;
    NumberPicker numberPicker;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        data = getIntent();
        user = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = firebaseFirestore.getInstance();
        editNoteDescription = findViewById(R.id.editNoteDescription);
        editNoteTitle = findViewById(R.id.editNoteTitle);
        numberPicker = findViewById(R.id.edit_note_number_picker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(6);
        int priority;

        String noteTitle = data.getStringExtra("title");
        String noteDescription = data.getStringExtra("description");
        String noteId = data.getStringExtra("noteId");
        priority = Integer.parseInt(data.getStringExtra("priority"));
        editNoteTitle.setText(noteTitle);
        editNoteDescription.setText(noteDescription);

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
                editNote();
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void editNote() {
        String nTitle = editNoteTitle.getText().toString();
        String nDesc = editNoteDescription.getText().toString();
        int priority = numberPicker.getValue();

        DocumentReference docref = firebaseFirestore.collection("Notebook").document(user.getUid()).collection("myNotes").document(data.getStringExtra("noteId"));

        Map<String,Object> note = new HashMap<>();
        note.put("title",nTitle);
        note.put("description",nDesc);
        note.put("priority", priority);

        docref.update(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(EditNote.this, "Note Saved.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), notesMain.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditNote.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                //spinner.setVisibility(View.VISIBLE);
            }
        });
//        if(nTitle.trim().isEmpty() || nDesc.trim().isEmpty()){
//            Toast.makeText(EditNote.this,"Please insert a title and description", Toast.LENGTH_LONG).show();
//            return;
//        }
//        CollectionReference notebookRef = FirebaseFirestore.getInstance()
//                .collection("Notebook");
//        notebookRef.add(new NoteTransfer(nTitle, nDesc, priority));
//        Toast.makeText(EditNote.this, "Note Edited and New Note saved", Toast.LENGTH_LONG).show();
//        startActivity(new Intent(getApplicationContext(), notesMain.class));
//        finish();
    }
}