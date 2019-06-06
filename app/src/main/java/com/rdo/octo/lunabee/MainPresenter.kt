package com.rdo.octo.lunabee

import android.os.Handler
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val view: MainView, private val service: UserService) : Callback<List<User>> {

    private var list = mutableListOf<User>()
    private var nextPage = 2

    override fun onFailure(call: Call<List<User>>, t: Throwable) {
        Log.e("YOLO TAG", "Error calling service", t)
        view.displayError()
    }

    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
        list.addAll(response.body() ?: emptyList())
        view.displayUsers(list)
    }

    fun getArticles() {
        service.getUsers().enqueue(this)
    }

    fun search(query: String) {
        view.lockSearchMode(query.isNotBlank())
        view.displayUsers(list.filter {
            it.first_name.toLowerCase().contains(query.toLowerCase())
        })
    }

    fun loadNextPage() {
        Handler().postDelayed({
            service.getUsers(nextPage++, 10).enqueue(this)
            service.getUsers(nextPage++, 10).enqueue(this)
        }, 2000)
    }
}