package com.angus.service_taxi.di

import org.koin.dsl.module
import com.angus.service_taxi.data.gateway.TaxiGateway
import com.angus.service_taxi.domain.gateway.ITaxiGateway

val GatewayModule = module {
    single<ITaxiGateway> { TaxiGateway(get()) }
}