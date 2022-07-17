package com.fakhrirasyids.notesapp.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: NoteEntity)

    @Query("UPDATE notes SET title =:title, description =:description, date =:date WHERE id =:id")
    fun update(title: String?, description: String?, date: String, id: Int)

    @Query("DELETE FROM notes WHERE id =:id")
    fun delete(id: Int)

    @Query("SELECT * FROM notes")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE title LIKE :query OR description LIKE :query")
    fun getSearchedNotes(query: String): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM notes WHERE id =:id")
    fun getNoteById(id: Int): LiveData<NoteEntity>
}