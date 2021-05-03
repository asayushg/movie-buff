package saini.ayush.moviebuff.network

import retrofit2.http.GET
import retrofit2.http.Query
import saini.ayush.moviebuff.model.PopularMovies

interface MoviesApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
    ): PopularMovies


    @GET("search/popular")
    suspend fun searchMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String,
        @Query("page") page: Int,
        @Query("query") query: String,
    ): PopularMovies

}