package com.angus.service_identity.domain.entity

data class UserOptions(
    val page: Int,
    val limit: Int,
    val query: String?,
    val permissions: List<Int>?,
    val country: List<String>?
)