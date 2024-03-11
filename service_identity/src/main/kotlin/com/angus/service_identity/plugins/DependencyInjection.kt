package com.angus.service_identity.plugins

import io.ktor.server.application.*
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import com.angus.service_identity.di.IdentityModule
import com.angus.service_identity.di.kmongoModule

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            IdentityModule().module,
            kmongoModule
        )
    }
}