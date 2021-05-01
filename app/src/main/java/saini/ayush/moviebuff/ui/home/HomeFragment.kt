package saini.ayush.moviebuff.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import saini.ayush.moviebuff.R
import saini.ayush.moviebuff.model.Movie
import saini.ayush.moviebuff.utils.Constants.SPAN_COUNT
import saini.ayush.moviebuff.utils.SpacesItemDecoration

class HomeFragment : Fragment(R.layout.fragment_home), MovieAdapter.Interaction,
    MovieAdapter.RetryInteraction {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: MovieAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        activity?.actionBar?.title = "Popular Movies"

        adapter = MovieAdapter(this, this)
        adapter = MovieAdapter(this, this)
        moviesRV.layoutManager = GridLayoutManager(context, SPAN_COUNT)
        moviesRV.addItemDecoration(SpacesItemDecoration(16))
        moviesRV.adapter = adapter

        adapter.submitList(viewModel.getMovies())

    }

    override fun onItemSelected(position: Int, item: Movie) {
        
    }

    override fun onItemSelected() {

    }


}