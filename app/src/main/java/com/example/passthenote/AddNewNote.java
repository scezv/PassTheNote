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
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.passthenote.model.NoteTransfer;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNewNote extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
       // getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_close_24);
        editTextTitle = findViewById(R.id.add_note_title);
        editTextDescription = findViewById(R.id.add_note_description);
        numberPickerPriority = findViewById(R.id.add_note_number_picker);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(6);
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
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();
        int priority = numberPickerPriority.getValue();

        if(title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this,"Please insert a title and description", Toast.LENGTH_LONG).show();
            return;
        }
        DocumentReference docref = FirebaseFirestore.getInstance().collection("Notebook").document(user.getUid()).collection("myNotes").document();
        Map<String,Object> note = new HashMap<>();
        note.put("title",title);
        note.put("description",description);
        note.put("priority", priority);

        docref.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AddNewNote.this, "Note Added.", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), notesMain.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddNewNote.this, "Error, Try again.", Toast.LENGTH_SHORT).show();
                //progressBarSave.setVisibility(View.VISIBLE);
            }
        });

//        CollectionReference notebookRef = FirebaseFirestore.getInstance()
//                .collection("Notebook");
//        notebookRef.add(new NoteTransfer(title, description, priority));
//        Toast.makeText(this, "Note Added", Toast.LENGTH_LONG).show();
//        finish();
    }
}