package com.swapnil.techstalwartsassignmet.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.swapnil.techstalwartsassignmet.model.data.LoginRequest
import com.swapnil.techstalwartsassignmet.model.network.NetworkModule
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class LoginViewModel(private val context: Context) : ViewModel() {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("my_shared_prefs", Context.MODE_PRIVATE)
    }
    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var token = mutableStateOf<String?>(loadTokenFromSharedPreferences())
    var errorMessage = mutableStateOf("")

    fun login() {
        viewModelScope.launch {
            try {
                val response = NetworkModule.loginApi.login(LoginRequest(email.value, password.value))
                token.value = response.token
                saveTokenToSharedPreferences(response.token)
                errorMessage.value = ""
            } catch (e: IOException) {
                // Handle network errors
                e.printStackTrace()
                errorMessage.value = "Network error: ${e.message}"
            } catch (e: HttpException) {
                // Handle HTTP errors
                e.printStackTrace()
                errorMessage.value = "Login failed: ${e.response()?.errorBody()?.string()}"
            }
        }
    }
    fun logout() {
        // Clear the token from memory and SharedPreferences
        token.value = null
        sharedPreferences.edit().remove("token").apply()
    }

    private fun saveTokenToSharedPreferences(token: String?) {
        sharedPreferences.edit().putString("token", token).apply()
    }

    private fun loadTokenFromSharedPreferences(): String? {
        return sharedPreferences.getString("token", null)
    }
}
class LoginViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}