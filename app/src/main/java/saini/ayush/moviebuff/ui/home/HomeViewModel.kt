package saini.ayush.moviebuff.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import saini.ayush.moviebuff.model.Movie
import saini.ayush.moviebuff.network.utils.DataState
import saini.ayush.moviebuff.network.utils.MainStateEvent
import saini.ayush.moviebuff.repository.Repository

class HomeViewModel
@ViewModelInject
constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val repository: Repository
) : ViewModel() {

    var movies = mutableListOf<Movie>()
    var page = 0

    private val _dataState: MutableLiveData<DataState<List<Movie>>> = MutableLiveData()

    val dataState: LiveData<DataState<List<Movie>>>
        get() = _dataState

    fun setStateEvent(mainStateEvent: MainStateEvent, page: Int) {
        viewModelScope.launch {
            when (mainStateEvent) {
                is MainStateEvent.GetPopularMoviesEvent -> {
                    repository.getPopularMovies(page)
                        .onEach { dataState ->
                            _dataState.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }

}