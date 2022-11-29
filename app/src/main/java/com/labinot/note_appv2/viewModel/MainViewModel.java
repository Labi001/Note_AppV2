package com.labinot.note_appv2.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.labinot.note_appv2.database.AppRepository;
import com.labinot.note_appv2.database.NoteEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    public LiveData<List<NoteEntity>> mNotes;
    private AppRepository mAppRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);

        mAppRepository=AppRepository.getInstance(application.getApplicationContext());
        mNotes = mAppRepository.mNotes;

    }

    public void addSampleData(){

        mAppRepository.addSampleData();
    }

    public void deleteAllNotes() {
        mAppRepository.deleteAllNotes();
    }

    public void deleteNote(int position) {

      mAppRepository.deleteNote(mNotes.getValue().get(position));

    }
}
