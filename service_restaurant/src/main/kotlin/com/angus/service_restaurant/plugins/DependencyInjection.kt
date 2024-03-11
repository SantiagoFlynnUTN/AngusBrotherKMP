package com.angus.service_restaurant.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import com.angus.service_restaurant.di.AngusClient

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(AngusClient)
    }
}