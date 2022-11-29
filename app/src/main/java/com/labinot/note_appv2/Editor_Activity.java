package com.labinot.note_appv2;

import static com.labinot.note_appv2.utils.Constants.EDITING_KEY;
import static com.labinot.note_appv2.utils.Constants.NOTE_ID_KEY;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.labinot.note_appv2.database.NoteEntity;
import com.labinot.note_appv2.utils.Constants;
import com.labinot.note_appv2.viewModel.EditorViewModel;

public class Editor_Activity extends AppCompatActivity {

    private EditText myNote_txt;
    private EditorViewModel editorViewModel;
    boolean mNewNote,mEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        myNote_txt = findViewById(R.id.note_text);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_check);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState != null)
            mEditing = savedInstanceState.getBoolean(EDITING_KEY);

        initViewModel();


    }

    private void initViewModel() {

        editorViewModel = new ViewModelProvider(this).get(EditorViewModel.class);

        editorViewModel.mLiveNotes.observe(this, new Observer<NoteEntity>() {
            @Override
            public void onChanged(NoteEntity noteEntity) {

                if (noteEntity != null && !mEditing)
                    myNote_txt.setText(noteEntity.getText());

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle(getString(R.string.new_note));
            mNewNote = true;

        } else {

            setTitle(getString(R.string.edit_note));
            editorViewModel.loadData(extras.getInt(NOTE_ID_KEY));

        }

    }

    private void saveAndReturn(){

        editorViewModel.saveNote(myNote_txt.getText().toString());
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(!mNewNote)
            getMenuInflater().inflate(R.menu.menu_editor,menu);



        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){

            saveAndReturn();
            return true;
        }else if(item.getItemId() == R.id.action_delete){

            editorViewModel.deleteNote();
            finish();

        }


           return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        saveAndReturn();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY,true);
        super.onSaveInstanceState(outState);
    }
}