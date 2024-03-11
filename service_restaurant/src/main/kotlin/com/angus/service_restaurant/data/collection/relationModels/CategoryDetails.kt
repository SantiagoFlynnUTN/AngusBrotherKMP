package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import com.angus.service_restaurant.data.collection.RestaurantCollection

@Serializable
data class CategoryDetails(
    @BsonId
    @Contextual
    @SerialName("_id")
    val id: ObjectId = ObjectId(),
    val name: String,
    val restaurants: MutableList<RestaurantCollection> = mutableListOf(),
    val image: String
)