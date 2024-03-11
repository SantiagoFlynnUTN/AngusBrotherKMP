package com.angus.service_chat.plugins

import io.ktor.server.application.*
import org.koin.ksp.generated.module
import org.koin.ktor.plugin.Koin
import com.angus.service_chat.di.ChatModule
import com.angus.service_chat.di.kmongoModule

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(
            ChatModule().module,
            kmongoModule
        )
    }
}