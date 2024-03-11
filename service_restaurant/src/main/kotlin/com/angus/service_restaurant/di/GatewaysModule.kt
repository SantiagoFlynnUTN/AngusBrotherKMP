package com.angus.service_restaurant.di

import org.koin.dsl.module
import com.angus.service_restaurant.data.gateway.RestaurantGateway
import com.angus.service_restaurant.data.gateway.RestaurantManagementGateway
import com.angus.service_restaurant.data.gateway.RestaurantOptionsGateway
import com.angus.service_restaurant.domain.gateway.IRestaurantGateway
import com.angus.service_restaurant.domain.gateway.IRestaurantManagementGateway
import com.angus.service_restaurant.domain.gateway.IRestaurantOptionsGateway

val GatewaysModule = module {
    single<IRestaurantGateway> { RestaurantGateway(get()) }
    single<IRestaurantOptionsGateway> { RestaurantOptionsGateway(get()) }
    single <IRestaurantManagementGateway>{ RestaurantManagementGateway(get()) }
}