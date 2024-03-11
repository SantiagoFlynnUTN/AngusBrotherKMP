package com.angus.service_chat.data.collection

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.bson.types.ObjectId

@Serializable
data class MessageCollection(
    @Contextual val id : ObjectId = ObjectId(),
    val senderId : String,
    val content : String,
    val time : Long
)