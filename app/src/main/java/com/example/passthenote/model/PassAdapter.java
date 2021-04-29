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
import com.example.passthenote.DetailsPass;
import com.example.passthenote.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PassAdapter extends FirestoreRecyclerAdapter<PassTransfer, PassAdapter.PassHolder> {
    private OnItemClickListener listener;
    private int color;
    String docId;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public PassAdapter(@NonNull FirestoreRecyclerOptions<PassTransfer> options) {
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
    protected void onBindViewHolder(@NonNull PassAdapter.PassHolder passHolder, int i, @NonNull PassTransfer passTransfer) {
        passHolder.passViewPlatform.setText(passTransfer.getPlatform());
        passHolder.passViewPassword.setText(passTransfer.getPassword());
        color = getRandomColor();
        docId = getSnapshots().getSnapshot(i).getId();
        passHolder.passCardView.setCardBackgroundColor(passHolder.itemView.getResources().getColor(color, null));
    }

    @NonNull
    @Override
    public PassAdapter.PassHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pass_item, parent, false);
        return new PassAdapter.PassHolder(v);
    }
    // deleting from recycler view
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    class PassHolder extends RecyclerView.ViewHolder{
        public View view;
        TextView passViewPlatform, passViewPassword;
        CardView passCardView;
        public PassHolder(@NonNull View itemView) {
            super(itemView);
            passViewPlatform = itemView.findViewById(com.example.passthenote.R.id.pass_view_platform);
            passViewPassword = itemView.findViewById(com.example.passthenote.R.id.pass_view_password);
            passCardView = itemView.findViewById(com.example.passthenote.R.id.passCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), DetailsPass.class);
                    i.putExtra("platform", passViewPlatform.getText().toString());
                    i.putExtra("password", passViewPassword.getText().toString());
//                    i.putExtra("priority", textViewPriority.getText().toString());
                    i.putExtra("code", color);
                    i.putExtra("passId", docId);
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
    public void setOnItemClickListener(PassAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
