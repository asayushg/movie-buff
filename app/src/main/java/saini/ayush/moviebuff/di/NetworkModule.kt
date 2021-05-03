package saini.ayush.moviebuff.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import saini.ayush.moviebuff.network.MoviesApi
import saini.ayush.moviebuff.utils.Constants
import javax.inject.Singleton


@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constants.BASE_URL)
    }

    @Singleton
    @Provides
    fun provideMoviesApi(retrofit: Retrofit.Builder): MoviesApi {
        return retrofit.build()
            .create(MoviesApi::class.java)
    }

}