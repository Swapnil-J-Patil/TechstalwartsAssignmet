package com.swapnil.techstalwartsassignmet.model.data

data class UserList(
    val page: Int,
    val per_page: Int,
    val total: Int,
    val total_pages: Int,
    val data: List<User>
)