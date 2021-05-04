package saini.ayush.moviebuff.ui.home

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.ColorInt
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
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

        supportActionBar?.title = getString(R.string.popularmovies)

        initUI()

        subscribeObservers()
        if (viewModel.page == 0) {
            resetMovies()
        }

        swipeRefresh.setOnRefreshListener {
            resetMovies()
        }

        checkIfNetworkAvail()

    }

    private fun checkIfNetworkAvail() {
        var have_WIFI = false
        var have_MobileData = false
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfos = connectivityManager.allNetworkInfo
        for (info in networkInfos) {
            if (info.typeName
                    .equals("WIFI", ignoreCase = true)
            ) if (info.isConnected) have_WIFI = true
            if (info.typeName
                    .equals("MOBILE DATA", ignoreCase = true)
            ) if (info.isConnected) have_MobileData = true
        }
        if (!have_WIFI && !have_MobileData) showSnackbar("No Internet Connection", moviesRV)
    }

    private fun resetMovies() {
        viewModel.movies.clear()
        adapter.notifyDataSetChanged()
        viewModel.page = 1
        viewModel.setStateEvent(MainStateEvent.GetPopularMoviesEvent, viewModel.page)
    }

    private fun initUI() {

        adapter = MovieAdapter(this, this)


        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = GridLayoutManager(this, Constants.SPAN_COUNT_PORTRAIT)

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == viewModel.movies.size - 1) Constants.SPAN_COUNT_PORTRAIT else 1
                }
            }

        } else {
            gridLayoutManager = GridLayoutManager(this, Constants.SPAN_COUNT_LANDSCAPE)

            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == viewModel.movies.size - 1) Constants.SPAN_COUNT_LANDSCAPE else 1
                }
            }
        }


        moviesRV.addItemDecoration(SpacesItemDecoration(16))
        moviesRV.layoutManager = gridLayoutManager
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        moviesRV.adapter = adapter

        moviesRV.viewTreeObserver.addOnPreDrawListener {
            startPostponedEnterTransition()
            true
        }

        retryBtnHome.setOnClickListener {
            retryBtnHome.visibility = View.GONE
            viewModel.page = 1
            viewModel.setStateEvent(MainStateEvent.GetPopularMoviesEvent, viewModel.page)
        }
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(this, Observer { dataState ->
            when (dataState) {
                is DataState.Success<List<Movie>> -> {
                    stopLoadingAnims()
                    appendMovies(dataState.data)
                }
                is DataState.Error -> {
                    stopLoadingAnims()
                    showNextItem()
                }
                is DataState.Loading -> {
                    shimmerFrameLayout.startShimmerAnimation()
                    shimmerFrameLayout.visibility = View.VISIBLE
                }
            }

        })
    }

    private fun stopLoadingAnims() {
        swipeRefresh.isRefreshing = false
        shimmerFrameLayout.stopShimmerAnimation()
        shimmerFrameLayout.visibility = View.GONE
    }

    private fun showLoading() {
        if (viewModel.movies.isNotEmpty()) {
            viewModel.movies.add(
                Movie(
                    id = 2
                )
            )
            submitMovies()
        }
    }

    private fun showNextItem() {
        removeExtraItems()
        if (viewModel.movies.isEmpty()) {
            retryBtnHome.visibility = View.VISIBLE
        } else {
            viewModel.movies.add(
                Movie(
                    id = 1
                )
            )
            submitMovies()
        }
    }


    private fun appendMovies(data: List<Movie>) {
        removeExtraItems()
        viewModel.movies.addAll(data)
        viewModel.movies.add(
            Movie(
                id = 4
            )
        )
        submitMovies()
    }

    private fun submitMovies() {
        adapter.submitList(viewModel.movies)
        adapter.notifyDataSetChanged()
    }

    private fun removeExtraItems() {

        val extras = listOf(
            Movie(id = 1),
            Movie(id = 2),
            Movie(id = 3),
            Movie(id = 4),
        )

        viewModel.movies.removeAll(extras)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)

        val searchItem = menu!!.findItem(R.id.app_bar_search)

        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search Movies"
        searchView.isIconified = false
        searchView.onActionViewCollapsed()
        searchView.setOnQueryTextListener(this)

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
            ),
            Pair(
                findViewById(android.R.id.statusBarBackground),
                Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME
            )
        )

        startActivity(intent, options.toBundle())

    }

    override fun onItemSelected(retry: Boolean) {
        if (retry) {
            viewModel.setStateEvent(MainStateEvent.GetPopularMoviesEvent, viewModel.page)
        } else {
            viewModel.page = viewModel.page + 1
            viewModel.setStateEvent(MainStateEvent.GetPopularMoviesEvent, viewModel.page)
        }
        removeExtraItems()
        showLoading()
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

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        Log.d("ayusht", newText.toString())
        return true
    }

}