package com.fakhrirasyids.notesapp.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fakhrirasyids.notesapp.database.NoteEntity
import com.fakhrirasyids.notesapp.database.repository.NoteRepository

class MainViewModel(application: Application) : ViewModel() {

    private val _searchedQuery = MutableLiveData<String?>()
    val searchedQuery: LiveData<String?> = _searchedQuery

    private val mNoteRepository: NoteRepository = NoteRepository(application)

    fun getAllNotes(): LiveData<List<NoteEntity>> {
        return mNoteRepository.getAllNotes()
    }

    fun getSearchedNotes(query: String): LiveData<List<NoteEntity>> {
        return mNoteRepository.getSearchedNotes(query)
    }

    fun setQuery(query: String?) {
        _searchedQuery.value = query
    }
}