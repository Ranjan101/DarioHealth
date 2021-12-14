package com.dario.health.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [MovieListEntity::class], version = 1)
abstract class FavoritesMovieListDatabase : RoomDatabase() {

    abstract fun movieListDao(): MovieListDAO

    companion object {
        @Volatile
        private var INSTANCE: FavoritesMovieListDatabase? = null

        fun getDatabase(context: Context): FavoritesMovieListDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context,
                        FavoritesMovieListDatabase::class.java,
                        "MovieListDB")
                        .build()
                }
            }
            return INSTANCE!!
        }
    }
}