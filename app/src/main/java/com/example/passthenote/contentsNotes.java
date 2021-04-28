package com.example.passthenote;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toolbar;

import com.example.passthenote.model.Adapter;
import com.example.passthenote.model.Note;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class contentsNotes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView nav_view;
    RecyclerView noteLists;
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contents_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Query query = firebaseFirestore.collection("notes").orderBy("title", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> allNotes =  new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();

        noteAdapter = new FirestoreRecyclerAdapter<Note, NoteViewHolder>(allNotes) {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, final int i, @NonNull final Note note) {
                noteViewHolder.nTitle.setText(note.getTitle());
                noteViewHolder.nContent.setText(note.getContent());
                final int code = getRandomColor();
                noteViewHolder.mCardView.setCardBackgroundColor(noteViewHolder.view.getResources().getColor(code, null));
                final String docId = noteAdapter.getSnapshots().getSnapshot(i).getId();

                noteViewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(v.getContext(), contentsNotes.class);
                        i.putExtra("title", note.getTitle());
                        i.putExtra("content", note.getContent());
                        i.putExtra("code", code);
                        i.putExtra("noteId", docId);
                        v.getContext().startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_view_layout,parent,false);
                return new NoteViewHolder(view);
            }
        };
        noteLists = findViewById(R.id.noteList);
        drawerLayout =(DrawerLayout) findViewById(R.id.drawer);
        nav_view =(NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();

//        List<String> titles = new ArrayList<>();
//        List<String> contents = new ArrayList<>();
//        titles.set(1, "First Note Title.");
//        contents.set(1, "First Note Content.");
//        titles.add("Second Note Title.");
//        contents.add("Second Note Content.");
//        titles.add("Third Note Title.");
//        contents.add("Third Note Content.");
//        adapter = new Adapter(titles, contents);

        noteLists.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        noteLists.setAdapter(noteAdapter);

        FloatingActionButton fab = findViewById(R.id.addNoteIcon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), AddNote.class));
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.navAddNotes:
                startActivity(new Intent(this, AddNote.class));
                break;
            default:
                Toast.makeText(this,"Clicked", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView nTitle, nContent;
        View view;
        CardView mCardView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            nTitle = (TextView) itemView.findViewById(R.id.titles);
            nContent = (TextView) itemView.findViewWithTag(R.id.content);
            mCardView = (CardView) itemView.findViewById(R.id.cardView);
            view = itemView;
        }
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(R.color.blue);
        colorCode.add(R.color.yellow);
        colorCode.add(R.color.skyblue);
        colorCode.add(R.color.lightPurple);
        colorCode.add(R.color.lightGreen);
        colorCode.add(R.color.gray);
        colorCode.add(R.color.pink);
        colorCode.add(R.color.red);
        colorCode.add(R.color.greenlight);
        colorCode.add(R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(noteAdapter != null){
            noteAdapter.stopListening();
        }
    }
}