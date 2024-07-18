package com.swapnil.techstalwartsassignmet.model.network

import androidx.annotation.Keep
import com.swapnil.techstalwartsassignmet.model.api.LoginAPI
import com.swapnil.techstalwartsassignmet.model.api.UserAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Keep
object NetworkModule {
    private const val BASE_URL = "https://reqres.in/api/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val userApi: UserAPI by lazy {
        retrofit.create(UserAPI::class.java)
    }

    val loginApi: LoginAPI by lazy {
        retrofit.create(LoginAPI::class.java)
    }
}

