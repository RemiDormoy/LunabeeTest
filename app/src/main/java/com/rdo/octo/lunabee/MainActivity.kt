package com.rdo.octo.lunabee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.cell_user.view.*
import android.app.SearchManager
import android.content.Context
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.widget.SearchView


class MainActivity : AppCompatActivity(), MainView, SearchView.OnQueryTextListener {

    private val adapter: MainAdapter by lazy {
        MainAdapter(::onCardClick, ::loadMore)
    }

    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainModule.inject(this)
        setContentView(R.layout.activity_main)
        mainRecyclerView.layoutManager = LinearLayoutManager(this)
        mainRecyclerView.adapter = adapter
        presenter.getArticles()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.search, menu)

        val searchItem = menu.findItem(R.id.action_search)

        val searchManager = this@MainActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager

        val searchView = searchItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this@MainActivity.componentName))
        searchView.setOnQueryTextListener(this)
        return super.onCreateOptionsMenu(menu)
    }

    override fun lockSearchMode(locked: Boolean) {
        adapter.lock(locked)
    }

    override fun displayError() {
        Toast.makeText(this, "Une erreur est survenue", Toast.LENGTH_SHORT).show()
    }

    override fun displayUsers(list: List<User>) {
        adapter.setList(list)
    }

    private fun onCardClick(user: User) {
        startActivity(DetailActivity.newIntent(user, this))
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            presenter.search(it)
        }
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let {
            presenter.search(it)
        }
        return true
    }

    private fun loadMore() {
        presenter.loadNextPage()
    }
}

class MainAdapter(private val onClick: (User) -> Unit, private val loadMore: () -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<User> = emptyList()
    private var locked = false

    fun setList(users: List<User>) {
        list = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val cell = if (viewType == 1) R.layout.cell_user else R.layout.progress_cell
        val view = LayoutInflater.from(parent.context).inflate(
            cell,
            parent,
            false
        )
        return object : RecyclerView.ViewHolder(
            view
        ) {}
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size) {
            0
        } else {
            1
        }
    }

    override fun getItemCount() = list.size + 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == list.size) {
            if (locked.not()) {
                holder.itemView.visibility = VISIBLE
                loadMore()
            } else {
                holder.itemView.visibility = GONE
            }
        } else {
            val user = list[position]
            holder.itemView.userContainer.setOnClickListener {
                onClick(user)
            }
            Picasso.get()
                .load(user.avatar)
                .into(holder.itemView.userImageView)
            holder.itemView.userTextView.text = listOf(user.first_name, user.last_name).joinToString(" ")

        }
    }

    fun lock(locked: Boolean) {
        this.locked = locked
    }
}

