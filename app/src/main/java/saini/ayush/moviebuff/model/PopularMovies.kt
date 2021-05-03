package saini.ayush.moviebuff.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PopularMovies(

    @SerializedName("page")
    @Expose
    val page: Int,

    @SerializedName("results")
    @Expose
    val movies: List<Movie>,

    @SerializedName("total_pages")
    @Expose
    val total_pages: Int,

    @SerializedName("total_results")
    @Expose
    val total_results: Int

)