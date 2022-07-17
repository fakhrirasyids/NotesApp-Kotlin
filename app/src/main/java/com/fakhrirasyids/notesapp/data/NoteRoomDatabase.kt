package com.fakhrirasyids.notesapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 2, exportSchema = true)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): NoteRoomDatabase {
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java, "note_database"
                    )
                        .build()
                }
            }
            return INSTANCE as NoteRoomDatabase
        }
    }
}