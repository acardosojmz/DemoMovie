package dev.cardoso.demomovie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.cardoso.demomovie.data.GenreConverters
import dev.cardoso.demomovie.model.Movie

@Database(entities = [Movie::class], version = 1)
@TypeConverters(GenreConverters::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}