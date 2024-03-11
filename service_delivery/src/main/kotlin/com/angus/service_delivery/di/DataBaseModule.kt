package com.angus.service_taxi.di

import com.angus.api_gateway.util.EnvConfig
import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo
import com.angus.service_taxi.data.DataBaseContainer

val DataBaseModule = module {
    single {
        val cluster = System.getenv("DB_CLUSTER") ?: EnvConfig.DB_CLUSTER
        val username = System.getenv("DB_USERNAME") ?: EnvConfig.DB_USERNAME
        val password = System.getenv("DB_PASSWORD") ?: EnvConfig.DB_PASSWORD
        val connectionString = ConnectionString("mongodb+srv://$username:$password@$cluster.mongodb.net/?retryWrites=true&w=majority")
        val settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        KMongo.createClient(settings).coroutine
    }
    single { DataBaseContainer(get()) }
}