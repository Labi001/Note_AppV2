package com.labinot.note_appv2;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.labinot.note_appv2.database.AppDatabase;
import com.labinot.note_appv2.database.NoteDao;
import com.labinot.note_appv2.database.NoteEntity;
import com.labinot.note_appv2.utils.SampleData;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DataBaseTest {

    public static final String TAG = "Junit";
    private AppDatabase mAppDatabase;
    private NoteDao noteDao;

    @Before
    public void createDB(){

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        mAppDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase.class).build();

        noteDao = mAppDatabase.noteDao();

        Log.i(TAG,"createDB()");

    }

    @After
    public void closeDB(){

        mAppDatabase.close();
        Log.i(TAG,"closeDB");
    }

    @Test
    public void createAndRetrieveNotes(){

        noteDao.InsertAll(SampleData.getNotes());
        int count = noteDao.getCount();
        Log.i(TAG,"createAndRetrieveNotes: count = "+count);

        assertEquals(SampleData.getNotes().size(),count);

    }

    @Test
    public void compareStrings(){

        noteDao.InsertAll(SampleData.getNotes());
        NoteEntity original = SampleData.getNotes().get(0);
        NoteEntity fromDB = noteDao.getNoteById(1);

        assertEquals(original.getText(),fromDB.getText());
        assertEquals(1,fromDB.getId());

    }

}
