package saini.ayush.moviebuff.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import saini.ayush.moviebuff.model.Movie

class HomeViewModel
@ViewModelInject
constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {


    fun getMovies(): List<Movie> {

        val movies = mutableListOf<Movie>()

        for (i in 100..110){
            movies.add(
                Movie(
                    i,
                    "Avengers Endgame The Enddscdknc",
                    "https://cdn.flickeringmyth.com/wp-content/uploads/2019/04/Endgame-Poster-Posse-5-600x911.jpg"
                )
            )
        }

        return movies
    }

}