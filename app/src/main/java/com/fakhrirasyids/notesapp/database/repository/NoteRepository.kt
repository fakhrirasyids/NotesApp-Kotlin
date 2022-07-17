package com.fakhrirasyids.notesapp.database.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.fakhrirasyids.notesapp.database.NoteDao
import com.fakhrirasyids.notesapp.database.NoteEntity
import com.fakhrirasyids.notesapp.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(application: Application) {
    private val mNoteDao: NoteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        mNoteDao = db.noteDao()
    }

    fun insert(note: NoteEntity) {
        executorService.execute { mNoteDao.insert(note) }
    }

    fun update(title: String?, description: String?, date: String, id: Int) {
        executorService.execute { mNoteDao.update(title, description, date, id) }
    }

    fun delete(id: Int) {
        executorService.execute { mNoteDao.delete(id) }
    }

    fun getAllNotes(): LiveData<List<NoteEntity>> = mNoteDao.getAllNotes()

    fun getSearchedNotes(query: String): LiveData<List<NoteEntity>> = mNoteDao.getSearchedNotes(query)

    fun getNoteById(id: Int): LiveData<NoteEntity> = mNoteDao.getNoteById(id)
}