package com.rdo.octo.lunabee

import retrofit2.Call
import retrofit2.http.GET

interface UserService {

    @GET("techtest/users")
    fun getUsers() : Call<List<User>>
}