package saini.ayush.moviebuff.cache

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieCacheEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun moviesDao(): MoviesDao

    companion object {
        val DATABASE_NAME: String = "movies_cache"
    }

}