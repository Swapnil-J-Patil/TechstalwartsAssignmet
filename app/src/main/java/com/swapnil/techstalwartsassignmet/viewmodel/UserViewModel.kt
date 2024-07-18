package com.swapnil.techstalwartsassignmet.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swapnil.techstalwartsassignmet.model.data.User
import com.swapnil.techstalwartsassignmet.model.network.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class UserViewModel : ViewModel() {
    var allUsers = mutableListOf<User>()
    var searchQuery = mutableStateOf("")

    private val _users = MutableStateFlow<List<User>>(emptyList())
    val users: StateFlow<List<User>> = _users

    init {
        fetchAllUsers()
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            var page = 1
            var totalPages: Int

            do {
                try {
                    val response = NetworkModule.userApi.getUsers(page)
                    totalPages = response.total_pages
                    allUsers.addAll(response.data)
                    page++
                } catch (e: IOException) {
                    e.printStackTrace()
                    break
                } catch (e: HttpException) {
                    e.printStackTrace()
                    break
                }
            } while (page <= totalPages)
            _users.value = allUsers.toList() // Update _users with a new list instance
        }
    }

    fun filterUsers(query: String) {
        searchQuery.value = query
        if (query.isEmpty()) {
            _users.value = allUsers
        } else {
            _users.value = allUsers.filter {
                it.first_name.contains(query, true) ||
                        it.last_name.contains(query, true) ||
                        it.email.contains(query, true)
            }
        }
    }
}

