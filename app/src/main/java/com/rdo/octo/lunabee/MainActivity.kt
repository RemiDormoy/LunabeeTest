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
import androidx.appcompat.widget.SearchView


class MainActivity : AppCompatActivity(), MainView, SearchView.OnQueryTextListener {

    private val adapter: MainAdapter by lazy {
        MainAdapter(::onCardClick)
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
}

class MainAdapter(private val onClick: (User) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: List<User> = emptyList()

    fun setList(users: List<User>) {
        list = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.cell_user,
            parent,
            false
        )
        return object : RecyclerView.ViewHolder(
            view
        ) {}
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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

