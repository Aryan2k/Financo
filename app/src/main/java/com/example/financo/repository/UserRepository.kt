package com.example.financo.repository

import com.example.financo.model.UserModel
import com.example.financo.service.UserApi
import com.example.financo.utils.Resource
import javax.inject.Inject

class UserRepository @Inject constructor(private val userApi: UserApi) {
    suspend fun loadUserData(userName: String): Resource<UserModel> {
        return try {
            Resource.success(userApi.getUserDetails(userName))
        } catch (exception: java.lang.Exception) {
            Resource.error(null, exception.message.toString())
        }
    }
}
