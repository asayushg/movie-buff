package saini.ayush.moviebuff.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.listitem_load_more.view.*
import kotlinx.android.synthetic.main.listitem_movie.view.*
import kotlinx.android.synthetic.main.listitem_retry.view.*
import saini.ayush.moviebuff.R
import saini.ayush.moviebuff.model.Movie

class MovieAdapter(
    private val interaction: Interaction? = null,
    private val retryInteraction: RetryInteraction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MOVIE = 0
        private const val TYPE_LOAD_MORE = 4
        private const val TYPE_NO_MORE_RESULTS = 3
        private const val TYPE_LOADING = 2
        private const val TYPE_RETRY = 1
    }

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {

        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TYPE_MOVIE -> {
                MovieViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.listitem_movie,
                        parent,
                        false
                    ),
                    interaction
                )
            }

            TYPE_LOAD_MORE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_load_more, parent, false)
                LoadMoreViewHolder(view, retryInteraction)
            }

            TYPE_LOADING -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_loading, parent, false)
                LoadingViewHolder(view)
            }

            TYPE_RETRY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_retry, parent, false)
                RetryViewHolder(view, retryInteraction)
            }

            TYPE_NO_MORE_RESULTS -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.listitem_no_more_results, parent, false)
                NoMoreResultsViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MovieViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is LoadMoreViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is RetryViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is LoadingViewHolder -> {
                holder.bind(differ.currentList[position])
            }
            is NoMoreResultsViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position].id) {
            1 -> {
                TYPE_RETRY
            }
            2 -> {
                TYPE_LOADING
            }
            3 -> {
                TYPE_NO_MORE_RESULTS
            }
            4 -> {
                TYPE_LOAD_MORE
            }
            else -> {
                TYPE_MOVIE
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Movie>) {
        differ.submitList(list)
    }

    class MovieViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            movie_title.text = item.title
            movie_img.clipToOutline = true
            Glide.with(this)
                .load(item.photo)
                .into(movie_img)


        }
    }

    class LoadingViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Movie) = with(itemView) {
        }
    }

    class NoMoreResultsViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: Movie) = with(itemView) {
        }
    }

    class RetryViewHolder
    constructor(
        itemView: View,
        private val interaction: RetryInteraction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) = with(itemView) {
            retryBtn.setOnClickListener {
                interaction?.onItemSelected()
            }
        }
    }

    class LoadMoreViewHolder
    constructor(
        itemView: View,
        private val interaction: RetryInteraction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Movie) = with(itemView) {
            loadMoreBtn.setOnClickListener {
                interaction?.onItemSelected()
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Movie)
    }

    interface RetryInteraction {
        fun onItemSelected()
    }

}
