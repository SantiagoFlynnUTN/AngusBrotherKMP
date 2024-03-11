package com.angus.service_location.di

import org.koin.dsl.module
import com.angus.service_location.data.SocketHandler

val appModule = module {
    single<SocketHandler> { SocketHandler() }
}