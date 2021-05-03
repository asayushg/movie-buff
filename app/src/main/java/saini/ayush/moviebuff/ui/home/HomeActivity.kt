package saini.ayush.moviebuff.ui.home

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import saini.ayush.moviebuff.R
import saini.ayush.moviebuff.model.Movie
import saini.ayush.moviebuff.network.utils.DataState
import saini.ayush.moviebuff.network.utils.MainStateEvent
import saini.ayush.moviebuff.ui.detail.DetailActivity
import saini.ayush.moviebuff.utils.Constants
import saini.ayush.moviebuff.utils.SpacesItemDecoration
import androidx.core.util.Pair
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.ActivityCompat

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), MovieAdapter.Interaction, MovieAdapter.RetryInteraction,
    SearchView.OnQueryTextListener {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var adapter: MovieAdapter
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        postponeEnterTransition()

        supportActionBar?.title = "Popular Movies"
        initUI()

        subscribeObservers()
        if (viewModel.page == 0) {
            viewModel.page = 1
            viewModel.setStateEvent(MainStateEvent.GetPopularMoviesEvent, viewModel.page)
        }
    }

    private fun initUI() {

        adapter = MovieAdapter(this, this)

        gridLayoutManager =
            if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                GridLayoutManager(this, Constants.SPAN_COUNT_PORTRAIT)
            else
                GridLayoutManager(this, Constants.SPAN_COUNT_LANDSCAPE)

        moviesRV.addItemDecoration(SpacesItemDecoration(16))
        moviesRV.layoutManager = gridLayoutManager
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        moviesRV.adapter = adapter
        postponeEnterTransition()
        moviesRV.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->

            when (dataState) {
                is DataState.Success<List<Movie>> -> {
                    shimmerFrameLayout.stopShimmerAnimation()
                    shimmerFrameLayout.visibility = View.GONE
                    Log.d("TAG", "subscribeObservers: ${dataState.data}")
                    appendMovies(dataState.data)
                }
                is DataState.Error -> {
                    shimmerFrameLayout.stopShimmerAnimation()
                    shimmerFrameLayout.visibility = View.GONE
                    Log.d("TAG", "subscribeObservers: ${dataState.exception.message}")
                    showSnackbar(dataState.exception.message.toString(), moviesRV)
                }
                is DataState.Loading -> {
                    shimmerFrameLayout.startShimmerAnimation()
                    shimmerFrameLayout.visibility = View.VISIBLE
                }
            }

        })
    }


    private fun appendMovies(data: List<Movie>) {
        viewModel.movies.addAll(data)
        adapter.submitList(data)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu!!.findItem(R.id.app_bar_search)

        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Movies"
        searchView.setOnQueryTextListener(this)
        searchView.isIconified = false
        searchView.onActionViewCollapsed()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        finish()
        return true

    }

    override fun onItemSelected(
        position: Int,
        imageView: ImageView,
        textView: TextView,
        item: Movie
    ) {

        val intent = Intent(this, DetailActivity::class.java)

        intent.putExtra(
            "movie",
            item
        )


        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            this,
            Pair<View, String>(
                imageView,
                item.poster_path
            ),
            Pair<View, String>(
                textView,
                item.title
            )
        )

        startActivity(intent, options.toBundle())

    }

    override fun onItemSelected() {

    }


    fun Snackbar.withColor(@ColorInt colorInt: Int): Snackbar {
        this.view.setBackgroundColor(colorInt)
        return this
    }

    fun showSnackbar(msg: String, view: View) {
        Snackbar
            .make(view, msg, Snackbar.LENGTH_LONG)
            .withColor(ContextCompat.getColor(this, R.color.red_500))
            .show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d("ayusht", " sub " + query.toString())
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("ayusht", newText.toString())
        return true
    }

}