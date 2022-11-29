package com.labinot.note_appv2.ui;

import static com.labinot.note_appv2.utils.Constants.NOTE_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labinot.note_appv2.Editor_Activity;
import com.labinot.note_appv2.R;
import com.labinot.note_appv2.database.NoteEntity;
import com.labinot.note_appv2.utils.Constants;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    private Context context;
    private List<NoteEntity> noteEntities;

    public NotesAdapter(Context context, List<NoteEntity> noteEntities) {
        this.context = context;
        this.noteEntities = noteEntities;
    }

    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notelist_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.ViewHolder holder, int position) {

        final NoteEntity noteEntity = noteEntities.get(position);

        holder.note_text.setText(noteEntity.getText());

        holder.fab_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, Editor_Activity.class);
                intent.putExtra(NOTE_ID_KEY,noteEntity.getId());
                context.startActivity(intent);

            }
        });

    }

    public void cancelDelete(int position) {
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {

        return noteEntities.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView note_text;
        private FloatingActionButton fab_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            note_text = itemView.findViewById(R.id.note_text);
            fab_btn = itemView.findViewById(R.id.fab);

        }
    }
}
