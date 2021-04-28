package com.example.passthenote.model;

import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.passthenote.DetailsNote;
import com.example.passthenote.contentsNotes;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends FirestoreRecyclerAdapter<NoteTransfer, NoteAdapter.NoteHolder> {
    private OnItemClickListener listener;
    private int color;
    String docId;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<NoteTransfer> options) {
        super(options);
    }

    private int getRandomColor() {
        List<Integer> colorCode = new ArrayList<>();
        colorCode.add(com.example.passthenote.R.color.blue);
        colorCode.add(com.example.passthenote.R.color.yellow);
        colorCode.add(com.example.passthenote.R.color.skyblue);
        colorCode.add(com.example.passthenote.R.color.lightPurple);
        colorCode.add(com.example.passthenote.R.color.lightGreen);
        colorCode.add(com.example.passthenote.R.color.gray);
        colorCode.add(com.example.passthenote.R.color.pink);
        colorCode.add(com.example.passthenote.R.color.red);
        colorCode.add(com.example.passthenote.R.color.greenlight);
        colorCode.add(com.example.passthenote.R.color.notgreen);

        Random randomColor = new Random();
        int number = randomColor.nextInt(colorCode.size());
        return colorCode.get(number);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onBindViewHolder(@NonNull NoteHolder noteHolder, int i, @NonNull NoteTransfer noteTransfer) {
        noteHolder.textViewTitle.setText(noteTransfer.getTitle());
        noteHolder.textViewDescription.setText(noteTransfer.getDescription());
        noteHolder.textViewPriority.setText(String.valueOf(noteTransfer.getPriority()));
        color = getRandomColor();
        docId = getSnapshots().getSnapshot(i).getId();
        noteHolder.noteCardView.setCardBackgroundColor(noteHolder.itemView.getResources().getColor(color, null));
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(com.example.passthenote.R.layout.note_item, parent, false);
        return new NoteHolder(v);
    }
    // deleting from recycler view
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class NoteHolder extends RecyclerView.ViewHolder{
        public View view;
        TextView textViewTitle, textViewDescription, textViewPriority;
        CardView noteCardView;
        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(com.example.passthenote.R.id.text_view_title);
            textViewDescription = itemView.findViewById(com.example.passthenote.R.id.text_view_description);
            textViewPriority = itemView.findViewById(com.example.passthenote.R.id.text_view_priority);
            noteCardView = itemView.findViewById(com.example.passthenote.R.id.noteCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), DetailsNote.class);
                    i.putExtra("title", textViewTitle.getText().toString());
                    i.putExtra("description", textViewDescription.getText().toString());
                    i.putExtra("priority", textViewPriority.getText().toString());
                    i.putExtra("code", color);
                    i.putExtra("noteId", docId);
                    v.getContext().startActivity(i);
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION && listener != null){
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
