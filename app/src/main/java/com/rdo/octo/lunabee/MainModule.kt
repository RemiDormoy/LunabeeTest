package com.rdo.octo.lunabee

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainModule {
    private lateinit var presenter: MainPresenter
    private lateinit var view: MainView

    fun inject(view: MainActivity) {
        this.view = view
        view.presenter = getPresenter()
    }

    private fun getPresenter(): MainPresenter {
        if (::presenter.isInitialized.not()) {
            presenter = MainPresenter(view, getService())
        }
        return presenter
    }

    private fun getService() = getRetrofit().create(UserService::class.java)

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl("http://server.lunabee.studio:11111/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}