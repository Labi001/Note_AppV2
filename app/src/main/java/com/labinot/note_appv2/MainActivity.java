package com.labinot.note_appv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.labinot.note_appv2.database.NoteEntity;
import com.labinot.note_appv2.ui.NotesAdapter;
import com.labinot.note_appv2.utils.SampleData;
import com.labinot.note_appv2.viewModel.MainViewModel;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<NoteEntity> noteData = new ArrayList<>();
    private NotesAdapter notesAdapter;
    private FloatingActionButton fab;
    private MainViewModel mainViewModel;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        fab =findViewById(R.id.fab_btn);

        noteData.addAll(SampleData.getNotes());
        initRecyclerView();
        initViewModel();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,Editor_Activity.class));
            }
        });



    }

    private void initViewModel() {

        final Observer<List<NoteEntity>> notesObserver = new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {

                noteData.clear();

                if(noteEntities != null)
                    noteData.addAll(noteEntities);

                if(notesAdapter==null){

                    notesAdapter = new NotesAdapter(MainActivity.this,noteData);
                    recyclerView.setAdapter(notesAdapter);
                }else{

                    notesAdapter.notifyDataSetChanged();
                }


            }
        };

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.mNotes.observe(this,notesObserver);

    }

    private void initRecyclerView() {

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(),layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        setItemTouchHelper(recyclerView);

    }

    private void setItemTouchHelper(RecyclerView recyclerView) {

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                dialogDelete(viewHolder.getAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);

    }

    private void dialogDelete(int position) {

        dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you wanna delete this Note ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainViewModel.deleteNote(position);
                    }
                })

                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notesAdapter.cancelDelete(position);

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        notesAdapter.cancelDelete(position);
                    }
                })
                .show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_add_sample_data){

            addSampleData();
            return true;

        }else if(id == R.id.action_delete_all){

            deleteAllNotes();
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void deleteAllNotes() {
        mainViewModel.deleteAllNotes();
    }

    private void addSampleData() {
        mainViewModel.addSampleData();
    }
}