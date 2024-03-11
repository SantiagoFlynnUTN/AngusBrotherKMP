package com.angus.service_restaurant.di

import org.koin.dsl.module
import com.angus.service_restaurant.api.utils.SocketHandler

val HelperModule = module {
    single<SocketHandler> { SocketHandler() }
}