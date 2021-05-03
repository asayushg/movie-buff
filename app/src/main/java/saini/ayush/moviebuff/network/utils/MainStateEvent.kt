package saini.ayush.moviebuff.network.utils

sealed class MainStateEvent {

    object GetPopularMoviesEvent : MainStateEvent()

}