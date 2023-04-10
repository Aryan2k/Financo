package com.example.financo.service

import com.example.financo.model.UserModel
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {

    @GET("?name=")
    suspend fun getUserDetails(@Query("name") cityName: String): UserModel

}