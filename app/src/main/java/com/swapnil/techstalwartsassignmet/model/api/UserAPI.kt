package com.swapnil.techstalwartsassignmet.model.api

import com.swapnil.techstalwartsassignmet.model.data.UserList
import retrofit2.http.GET
import retrofit2.http.Query

interface UserAPI {
    @GET("users")
    suspend fun getUsers(@Query("page") page: Int): UserList
}
