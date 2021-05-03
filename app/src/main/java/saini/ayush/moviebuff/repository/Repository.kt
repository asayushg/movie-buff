package saini.ayush.moviebuff.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import saini.ayush.moviebuff.model.Movie
import saini.ayush.moviebuff.network.MoviesApi
import saini.ayush.moviebuff.network.utils.DataState
import saini.ayush.moviebuff.utils.Constants

class Repository(
    private val moviesApi: MoviesApi
) {

    suspend fun getPopularMovies(page: Int): Flow<DataState<List<Movie>>> = flow {

        emit(DataState.Loading)

        try {

            val popularMovies = moviesApi.getPopularMovies(
                Constants.API_KEY,
                Constants.LANGUAGE,
                page
            )


            //  val news = networkMapper.mapFromEntityList(networkTopHeadlines.articles)

            //  for (newsItem in news) {
            //      newsDao.insert(cacheMapper.mapToEntity(newsItem))
            //  }

            //  val cachedNews = newsDao.get()
            emit(DataState.Success(popularMovies.movies))


        } catch (e: Exception) {
            Log.d("repo", e.toString())
            // val cachedNews = newsDao.get()
            // emit(DataState.Success(cacheMapper.mapFromEntityList(cachedNews)))
            emit(DataState.Error(java.lang.Exception("Something went wrong")))
        }

    }

}