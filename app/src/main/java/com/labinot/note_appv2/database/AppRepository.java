package com.labinot.note_appv2.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.labinot.note_appv2.utils.SampleData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {

    private static AppRepository ourInstance;

    public LiveData<List<NoteEntity>> mNotes;
    private final AppDatabase mDB;
    private final Executor executor = Executors.newSingleThreadExecutor();

    public AppRepository(Context context) {
        mDB = AppDatabase.getInstance(context);
        mNotes = getAllNotes();
    }


    public static AppRepository getInstance(Context context){

        if(ourInstance == null)
            ourInstance = new AppRepository(context);

        return ourInstance;

    }

    private LiveData<List<NoteEntity>> getAllNotes() {

        return mDB.noteDao().getAll();
    }

    public void addSampleData(){

        executor.execute(new Runnable() {
            @Override
            public void run() {

                mDB.noteDao().InsertAll(SampleData.getNotes());
            }
        });

    }

    public void deleteAllNotes() {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                mDB.noteDao().deleteAll();
            }
        });

    }

    public void insertNote(NoteEntity note) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                mDB.noteDao().InsertNote(note);
            }
        });

    }

    public NoteEntity getNoteById(int Id) {

        return mDB.noteDao().getNoteById(Id);
    }

    public void deleteNote(NoteEntity note) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                mDB.noteDao().deleteNotes(note);
            }
        });

    }
}
