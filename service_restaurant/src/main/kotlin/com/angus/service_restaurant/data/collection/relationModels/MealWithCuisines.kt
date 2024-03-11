package com.angus.service_restaurant.data.collection.relationModels

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import com.angus.service_restaurant.data.collection.CuisineCollection
import com.angus.service_restaurant.data.collection.VariationCollection
import java.util.*

@Serializable
data class MealWithCuisines(
    @SerialName("_id")
    @BsonId
    @Contextual
    val id: ObjectId = ObjectId(),
    val name: String,
    val description: String,
    val isDeleted: Boolean = false,
    val price: Double,
    val currency:String,
    @Contextual
    val restaurantId: ObjectId,
    val cuisines: List<CuisineCollection>,
    val image : String,
    val variations: List<VariationCollection>?
)