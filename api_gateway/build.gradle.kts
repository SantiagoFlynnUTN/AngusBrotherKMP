val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val koin_version: String by project
val koin_ktor: String by project
val koin_ksp_version: String by project
val status_pages: String by project

plugins {
    kotlin("jvm") version "1.8.10"
    id("io.ktor.plugin") version "2.2.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.10"
    id("application")
    id("com.google.devtools.ksp") version "1.8.21-1.0.11"
}

group = "com.angus.api_gateway"
version = "0.0.1"
application {
    mainClass.set("com.angus.api_gateway.ApplicationKt")
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

ktor {
    fatJar {
        archiveFileName.set("api_gateway.jar")
    }
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("io.ktor:ktor-server-core-jvm:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktor_version")
    //ktor-server
    implementation("io.ktor:ktor-server-call-logging-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-swagger:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")
    implementation("io.ktor:ktor-server-netty-jvm:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests-jvm:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("io.ktor:ktor-server-config-yaml:$ktor_version")
    // koin
    implementation("io.insert-koin:koin-ktor:$koin_ktor")
    implementation("io.insert-koin:koin-core:$koin_version")
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.8.10-1.0.9")
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version")
    // koinLogger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")
    //security
    implementation("commons-codec:commons-codec:1.15")
    //ktor-client
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-client-logging-jvm:1.5.0")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.11")
    // Status pages error handler
    implementation("io.ktor:ktor-server-status-pages-jvm:$status_pages")

    implementation("io.ktor:ktor-server-websockets-jvm:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")

    implementation("aws.sdk.kotlin:s3:0.25.0-beta")
    implementation(group = "org.slf4j", name = "slf4j-api", version = "2.0.0")
    implementation(group = "org.slf4j", name = "slf4j-simple", version = "2.0.0")

}

