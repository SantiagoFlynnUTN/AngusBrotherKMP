package com.angus.common.domain.entity


data class User(
    val id: String,
    val fullName: String,
    val username:String,
    val email:String,
    val country:String,
    val permission:List<Permission>,
    val imageUrl: String,
)
