package com.angus.service_taxi.di

import org.koin.dsl.module
import com.angus.service_taxi.domain.usecase.ClientTripsManagementUseCase
import com.angus.service_taxi.domain.usecase.DriverTripsManagementUseCase
import com.angus.service_taxi.domain.usecase.IClientTripsManagementUseCase
import com.angus.service_taxi.domain.usecase.IDriverTripsManagementUseCase
import com.angus.service_taxi.domain.usecase.IManageTaxiUseCase
import com.angus.service_taxi.domain.usecase.IManageTripsUseCase
import com.angus.service_taxi.domain.usecase.ManageTaxiUseCase
import com.angus.service_taxi.domain.usecase.ManageTripsUseCase
import com.angus.service_taxi.domain.usecase.utils.IValidations
import com.angus.service_taxi.domain.usecase.utils.Validations

val UseCasesModule = module {
    single<IClientTripsManagementUseCase> { ClientTripsManagementUseCase(get(), get()) }
    single<IDriverTripsManagementUseCase> { DriverTripsManagementUseCase(get()) }
    single<IManageTripsUseCase> { ManageTripsUseCase(get()) }
    single<IManageTaxiUseCase> { ManageTaxiUseCase(get(), get()) }
    single<IValidations> { Validations() }
}