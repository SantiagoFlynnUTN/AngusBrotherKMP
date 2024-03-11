package com.angus.api_gateway.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.angus.api_gateway.data.model.authenticate.TokenType
import io.ktor.server.response.*
import com.angus.api_gateway.util.EnvConfig

fun Application.configureJWTAuthentication() {

    val jwtSecret = System.getenv("JWT_SECRET") ?: EnvConfig.JWT_SECRET
    val jwtDomain = System.getenv("JWT_ISSUER") ?: EnvConfig.JWT_ISSUER
    val jwtAudience = System.getenv("JWT_AUDIENCE") ?: EnvConfig.JWT_AUDIENCE
    val jwtRealm = System.getenv("JWT_REALM") ?: EnvConfig.JWT_REALM

    authentication {
        jwt("auth-jwt") {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("tokenType", TokenType.ACCESS_TOKEN.name)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else
                    null
            }
            respondUnauthorized()
        }

        jwt("refresh-jwt") {
            realm = jwtRealm
            verifier(
                JWT.require(Algorithm.HMAC256(jwtSecret))
                    .withAudience(jwtAudience)
                    .withIssuer(jwtDomain)
                    .withClaim("tokenType", TokenType.REFRESH_TOKEN.name)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) {
                    JWTPrincipal(credential.payload)
                } else
                    null
            }

            respondUnauthorized()
        }
    }
}

private fun JWTAuthenticationProvider.Config.respondUnauthorized() {
    challenge { _, _ ->
        call.respond(UnauthorizedResponse())
    }
}