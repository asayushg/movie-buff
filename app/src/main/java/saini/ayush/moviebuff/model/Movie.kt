package saini.ayush.moviebuff.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Movie(

    @SerializedName("adult")
    @Expose
    val adult: Boolean = false,

    @SerializedName("backdrop_path")
    @Expose
    val backdrop_path: String? = "",

    @SerializedName("genre_ids")
    @Expose
    val genre_ids: List<Int> = listOf(),

    @SerializedName("id")
    @Expose
    val id: Int = -1,

    @SerializedName("original_language")
    @Expose
    val original_language: String = "en",

    @SerializedName("original_title")
    @Expose
    val original_title: String = "",

    @SerializedName("overview")
    @Expose
    val overview: String = "",

    @SerializedName("popularity")
    @Expose
    val popularity: Double = 0.0,

    @SerializedName("poster_path")
    @Expose
    val poster_path: String = "",

    @SerializedName("release_date")
    @Expose
    val release_date: String = "",

    @SerializedName("title")
    @Expose
    val title: String = "",

    @SerializedName("video")
    @Expose
    val video: Boolean = false,

    @SerializedName("vote_average")
    @Expose
    val vote_average: Double = 0.0,

    @SerializedName("vote_count")
    @Expose
    val vote_count: Int = 0
) : Serializable