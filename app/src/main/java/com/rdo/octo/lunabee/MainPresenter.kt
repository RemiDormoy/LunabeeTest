package com.rdo.octo.lunabee

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val view: MainView, private val service: UserService) :
    Callback<List<User>> {
    override fun onFailure(call: Call<List<User>>, t: Throwable) {
        Log.e("YOLO TAG", "Error calling service", t)
        view.displayError()
    }

    override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
        view.displayUsers(response.body() ?: emptyList())
    }

    fun getArticles() {
        service.getUsers().enqueue(this)
    }
}