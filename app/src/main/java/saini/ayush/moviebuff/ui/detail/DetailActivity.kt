package saini.ayush.moviebuff.ui.detail

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.activity_detail.*
import saini.ayush.moviebuff.R
import saini.ayush.moviebuff.model.Movie
import saini.ayush.moviebuff.utils.Constants

class DetailActivity : AppCompatActivity() {

    lateinit var movie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        postponeEnterTransition()

        movie = intent.getSerializableExtra("movie") as Movie


        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_layout.title = movie.title

        initUi()

    }

    private fun initUi() {

        movie_img.apply {
            transitionName = movie.poster_path
            clipToOutline = true
            Glide.with(this)
                .load(Constants.IMAGE_BASE_URL + movie.poster_path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        p0: GlideException?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        p0: Drawable?,
                        p1: Any?,
                        p2: Target<Drawable>?,
                        p3: DataSource?,
                        p4: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(this)
        }

        movie_poster.apply {
            Glide.with(this)
                .load(Constants.IMAGE_BASE_URL + movie.backdrop_path)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)
        }

        movie_title.apply {
            transitionName = movie.title
            text = movie.title
        }

        movie_rating.text = movie.vote_average.toString()
        movie_detail.text = movie.release_date
        movie_overview.text = movie.overview

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            android.R.id.home -> {
                finishAfterTransition()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}