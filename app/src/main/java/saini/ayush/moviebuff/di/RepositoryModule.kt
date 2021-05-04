package saini.ayush.moviebuff.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import saini.ayush.moviebuff.cache.CacheMapper
import saini.ayush.moviebuff.cache.MoviesDao
import saini.ayush.moviebuff.network.MoviesApi
import saini.ayush.moviebuff.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRecipeRepository(
        moviesDao: MoviesDao,
        moviesApi: MoviesApi,
        cacheMapper: CacheMapper,
    ): Repository {
        return Repository(moviesDao, moviesApi, cacheMapper)
    }

}