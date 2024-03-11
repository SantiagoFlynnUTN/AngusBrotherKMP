package com.angus.service_taxi.di

import org.koin.dsl.module
import com.angus.service_taxi.api.util.SocketHandler

val SocketModule = module {
    single<SocketHandler> { SocketHandler() }
}