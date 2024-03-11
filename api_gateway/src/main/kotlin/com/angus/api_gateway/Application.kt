package com.angus.api_gateway

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.angus.api_gateway.data.model.authenticate.TokenConfiguration
import com.angus.api_gateway.plugins.*
import com.angus.api_gateway.util.EnvConfig

fun main() {
    embeddedServer(
        Netty,
        port = System.getenv("API_GATEWAY_PORT")?.toInt() ?: EnvConfig.API_GATEWAY_PORT,
        host = System.getenv("API_GATEWAY_HOST") ?: EnvConfig.API_GATEWAY_HOST,
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    val secret = System.getenv("JWT_SECRET") ?: EnvConfig.JWT_SECRET
    val issuer = System.getenv("JWT_ISSUER") ?: EnvConfig.JWT_ISSUER
    val audience = System.getenv("JWT_AUDIENCE") ?: EnvConfig.JWT_AUDIENCE

    val tokenConfig = TokenConfiguration(
        secret = secret,
        issuer = issuer,
        audience = audience,
        accessTokenExpirationTimestamp = 356L * 24L * 60L * 60L * 1000L,
        refreshTokenExpirationTimestamp = 356L * 24L * 60L * 60L * 1000L,
    )
    configureStatusPages()
    configureSockets()
    configureJWTAuthentication()
    configureDependencyInjection()
    configureSerialization()
    configureMonitoring()
    configureRouting(tokenConfig)
}
