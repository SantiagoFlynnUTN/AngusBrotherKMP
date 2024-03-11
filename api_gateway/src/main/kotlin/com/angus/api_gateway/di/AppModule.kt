package com.angus.api_gateway.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [ApiClientModule::class])
@ComponentScan("com.angus.api_gateway")
class AppModule