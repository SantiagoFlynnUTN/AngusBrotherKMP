package com.angus.service_notification.data.collection

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class NotificationHistoryCollection(
    @BsonId
    @SerialName("_id")
    @Contextual
    val id: ObjectId = ObjectId(),
    @SerialName("user_id")
    val userId: String? = null,
    @SerialName("topicId")
    val topicId: String? = null,
    @SerialName("sender")
    val sender: Int,
    @SerialName("topic")
    val topic: String? = null,
    @SerialName("title")
    val title: String,
    @SerialName("body")
    val body: String,
    @SerialName("date")
    val date: Long,
    @SerialName("is_deleted")
    val isDeleted: Boolean = false,
)
