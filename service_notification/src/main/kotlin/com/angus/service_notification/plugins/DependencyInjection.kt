package com.angus.service_notification.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import com.angus.service_notification.di.NotificationModule
import com.angus.service_notification.di.firebaseModule
import com.angus.service_notification.di.kmongoModule

fun Application.configureDependencyInjection() {
    install(Koin) {
        slf4jLogger()
        modules(
            NotificationModule().module,
            kmongoModule,
            firebaseModule
        )
    }
}