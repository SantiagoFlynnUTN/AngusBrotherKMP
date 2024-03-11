package com.angus.service_restaurant.di

import org.koin.dsl.module


val AngusClient = module {
    includes(
        DataBaseModule,
        UseCasesModule,
        GatewaysModule,
        HelperModule
    )
}

