package com.angus.service_notification.di

import com.angus.api_gateway.util.EnvConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.mongodb.ConnectionString
import com.mongodb.reactivestreams.client.MongoClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

@Module
@ComponentScan("com.angus.service_notification")
class NotificationModule

val kmongoModule = module {
    single {
        val cluster = System.getenv("DB_CLUSTER") ?: EnvConfig.DB_CLUSTER
        val username = System.getenv("DB_USERNAME") ?: EnvConfig.DB_USERNAME
        val password = System.getenv("DB_PASSWORD") ?: EnvConfig.DB_PASSWORD
        val connectionString = ConnectionString("mongodb+srv://$username:$password@$cluster.mongodb.net/?retryWrites=true&w=majority")
        KMongo.createClient(connectionString)
    }

    single {
        val DATA_BASE_NAME = "TEST_DB"
        get<MongoClient>().coroutine.getDatabase(DATA_BASE_NAME)
    }
}

val firebaseModule = module {
    single {
        FirebaseMessaging.getInstance()
    }
}
