package saini.ayush.moviebuff.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import saini.ayush.moviebuff.cache.MovieDatabase
import saini.ayush.moviebuff.cache.MoviesDao
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(
            context,
            MovieDatabase::class.java,
            MovieDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsDao(movieDatabase: MovieDatabase): MoviesDao {
        return movieDatabase.moviesDao()
    }

}