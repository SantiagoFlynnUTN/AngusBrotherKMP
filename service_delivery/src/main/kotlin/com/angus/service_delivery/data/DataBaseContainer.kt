package com.angus.service_taxi.data

import org.litote.kmongo.coroutine.CoroutineClient
import com.angus.service_taxi.data.collection.TaxiCollection
import com.angus.service_taxi.data.collection.TripCollection

class DataBaseContainer(client: CoroutineClient) {
    private val database by lazy { client.getDatabase(DATA_BASE_NAME) }

    val tripCollection by lazy { database.getCollection<TripCollection>(TRIP_COLLECTION_NAME) }

    val taxiCollection by lazy { database.getCollection<TaxiCollection>(TAXI_COLLECTION_NAME) }

    companion object {
        val DATA_BASE_NAME = "TEST_DB"
        const val TAXI_COLLECTION_NAME = "taxi"
        const val TRIP_COLLECTION_NAME = "trip"
    }
}