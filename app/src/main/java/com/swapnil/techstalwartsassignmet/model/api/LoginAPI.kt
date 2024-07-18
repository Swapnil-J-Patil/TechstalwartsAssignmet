package com.swapnil.techstalwartsassignmet.model.api

import androidx.annotation.Keep
import com.swapnil.techstalwartsassignmet.model.data.LoginRequest
import com.swapnil.techstalwartsassignmet.model.data.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}