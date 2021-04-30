package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.example.passthenote.model.NoteAdapter;
import com.example.passthenote.model.NoteTransfer;
import com.example.passthenote.model.PassAdapter;
import com.example.passthenote.model.PassTransfer;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class passMain extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PassAdapter passAdapter;
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_main);
        FloatingActionButton buttonAddPass = findViewById(R.id.addPassButton);
        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        buttonAddPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddNewPass.class));
            }
        });
        setUpRecyclerView();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = db.collection("Passbook").document(user.getUid()).collection("myPassword");
        FirestoreRecyclerOptions<PassTransfer> options = new FirestoreRecyclerOptions.Builder<PassTransfer>()
                .setQuery(query, PassTransfer.class)
                .build();

        passAdapter = new PassAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(passAdapter);

        // swipe to delete from recycler view
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                passAdapter.deleteItem(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

        passAdapter.setOnItemClickListener(new PassAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                PassTransfer note = documentSnapshot.toObject(PassTransfer.class);
                String id = documentSnapshot.getId();
                //final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();
                String path = documentSnapshot.getReference().getPath();
//                Toast.makeText(notesMain.this,
//                       "Position "+ position + " ID: "+ id, Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // menu selection on three dots
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // takes to password update password

        if(item.getItemId() == R.id.switchManager){
            startActivity(new Intent(getApplicationContext(), ChooseOption.class));
            finish();
        }
        if(item.getItemId() == R.id.resetPassword){
            if(!firebaseAuth.getCurrentUser().isAnonymous()) {
                startActivity(new Intent(getApplicationContext(), ResetPassword.class));
                finish();
            } else {
                Toast.makeText(passMain.this,"You are using a temporary account so this feature cannot be used", Toast.LENGTH_SHORT).show();
            }
        }
        // updates e-mail on the record
        if(item.getItemId() == R.id.updateEmailMenu){
            if(!firebaseAuth.getCurrentUser().isAnonymous()) {
                View view = inflater.inflate(R.layout.update_email, null);
                reset_alert.setTitle("Update E-mail Address?")
                        .setMessage("Enter the E-mail Address you want to change to")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // validate email of the user
                                EditText email = view.findViewById(R.id.updateEmail);
                                if (email.getText().toString().isEmpty()) {
                                    email.setError("Required Field");
                                    return;
                                }
                                // validate password of the user
                                EditText password = view.findViewById(R.id.updateEmailPass);

                                String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                                firebaseAuth.signInWithEmailAndPassword(userEmail, password.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        firebaseAuth.getCurrentUser().updateEmail(email.getText().toString());
                                        Toast.makeText(passMain.this, "E-mail Updated", Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(passMain.this, "Password Incorrect", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();
            } else {
                Toast.makeText(passMain.this,"You are using a temporary account so this feature cannot be used", Toast.LENGTH_SHORT).show();
            }
        }
        if(item.getItemId() == R.id.logoutAccount){
            checkUser();
        }
        return super.onOptionsItemSelected(item);
    }
    private void checkUser() {
        // user is anonymous or not
        if(user.isAnonymous()) {
            displayAlert();
        } else {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }
    }

    private void displayAlert() {
        AlertDialog.Builder warning  = new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage("You are logged in with temporary account, logging out will delete all your saved notes and passwords!!")
                .setPositiveButton("Sync Note", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), Registration.class));
                        finish();
                    }
                }).setNegativeButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ToDo: delete all the notes created by the logged in user
                        //ToDo: delete the anon user
                        // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(), Login.class));
                                finish();
                            }
                        });
                    }
                });
        warning.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        passAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        passAdapter.stopListening();
    }
}