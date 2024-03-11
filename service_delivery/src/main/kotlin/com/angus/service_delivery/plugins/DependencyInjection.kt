package com.angus.service_taxi.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ktor.plugin.Koin
import com.angus.service_taxi.di.AppModules

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(AppModules)
    }
}