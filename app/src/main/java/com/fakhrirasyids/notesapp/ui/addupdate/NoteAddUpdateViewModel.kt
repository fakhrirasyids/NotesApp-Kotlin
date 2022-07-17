package com.fakhrirasyids.notesapp.ui.addupdate

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhrirasyids.notesapp.database.NoteEntity
import com.fakhrirasyids.notesapp.database.repository.NoteRepository

class NoteAddUpdateViewModel(application: Application): ViewModel() {
    private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getNoteById(id: Int): LiveData<NoteEntity> = mNoteRepository.getNoteById(id)

    fun insert(note: NoteEntity) {
        mNoteRepository.insert(note)
    }

    fun update(title: String?, description: String?, date: String, id: Int) {
        mNoteRepository.update(title, description, date, id)
    }

    fun delete(id: Int) {
        mNoteRepository.delete(id)
    }
}