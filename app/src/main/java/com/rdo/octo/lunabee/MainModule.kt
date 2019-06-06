package com.rdo.octo.lunabee

import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MainModule {
    private lateinit var presenter: MainPresenter
    private lateinit var view: MainActivity

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

    private fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(ChuckInterceptor(view))
            .build()
        return Retrofit.Builder()
            .baseUrl("http://server.lunabee.studio:11111/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}