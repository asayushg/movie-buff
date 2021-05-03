package saini.ayush.moviebuff.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import saini.ayush.moviebuff.network.MoviesApi
import saini.ayush.moviebuff.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RepositoryModule{

    @Singleton
    @Provides
    fun provideRecipeRepository(
        moviesApi: MoviesApi
    ) : Repository {
        return Repository(moviesApi)
    }

}