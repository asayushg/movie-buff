package saini.ayush.moviebuff.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newsCacheEntity: MovieCacheEntity): Long

    @Query("SELECT * FROM movies ORDER BY popularity DESC")
    suspend fun get(): List<MovieCacheEntity>

}