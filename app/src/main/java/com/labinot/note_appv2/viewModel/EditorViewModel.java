package com.labinot.note_appv2.viewModel;

import android.app.Application;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.labinot.note_appv2.database.AppRepository;
import com.labinot.note_appv2.database.NoteEntity;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class EditorViewModel extends AndroidViewModel {

    public MutableLiveData <NoteEntity> mLiveNotes = new MutableLiveData<>();
    private AppRepository appRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public EditorViewModel(Application application) {
        super(application);

        appRepository = AppRepository.getInstance(getApplication());

    }

    public void loadData(final int noteId){

        executor.execute(new Runnable() {
            @Override
            public void run() {

                NoteEntity note =appRepository.getNoteById(noteId);
                mLiveNotes.postValue(note);
            }
        });

    }

    public void saveNote(String noteText){

        NoteEntity note = mLiveNotes.getValue();

        if(note == null){

            if(TextUtils.isEmpty(noteText.trim()))
                return;
            note = new NoteEntity(new Date(),noteText.trim());

        }else{

            note.setText(noteText.trim());
        }

        appRepository.insertNote(note);

    }

    public void deleteNote(){

        appRepository.deleteNote(mLiveNotes.getValue());

    }

}
