package saini.ayush.moviebuff.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import saini.ayush.moviebuff.cache.CacheMapper
import saini.ayush.moviebuff.cache.MoviesDao
import saini.ayush.moviebuff.model.Movie
import saini.ayush.moviebuff.network.MoviesApi
import saini.ayush.moviebuff.network.utils.DataState
import saini.ayush.moviebuff.utils.Constants

class Repository(
    private val moviesDao: MoviesDao,
    private val moviesApi: MoviesApi,
    var cacheMapper: CacheMapper,
) {

    suspend fun getPopularMovies(page: Int): Flow<DataState<List<Movie>>> = flow {

        emit(DataState.Loading)

        try {

            val popularMovies = moviesApi.getPopularMovies(
                Constants.API_KEY,
                Constants.LANGUAGE,
                page
            )

            val movies = cacheMapper.mapToEntityList(popularMovies.movies)

            for (movieItem in movies) {
                moviesDao.insert(movieItem)
            }

            emit(DataState.Success(popularMovies.movies))
        } catch (e: Exception) {
            val cachedMovies = moviesDao.get()
            if (cachedMovies.isNotEmpty()) emit(
                DataState.Success(
                    cacheMapper.mapFromEntityList(
                        cachedMovies
                    )
                )
            )
            else emit(
                DataState.Error(
                    java.lang.Exception("Something Went Wrong")
                )
            )
        }

    }

}