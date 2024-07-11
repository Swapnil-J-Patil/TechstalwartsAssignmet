package com.swapnil.techstalwartsassignmet.model.di

import com.swapnil.techstalwartsassignmet.model.api.LoginAPI
import com.swapnil.techstalwartsassignmet.model.api.UserAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object LoginModule {
    private const val BASE_URL = "https://reqres.in/api/"

    val api: LoginAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LoginAPI::class.java)
    }
}