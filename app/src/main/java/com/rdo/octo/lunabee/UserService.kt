package com.rdo.octo.lunabee

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {

    @GET("techtest/users")
    fun getUsers(@Query("page") page: Int = 1, @Query("pageSize") pageSize: Int = 20): Call<List<User>>
}