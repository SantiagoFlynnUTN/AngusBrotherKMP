package com.angus.service_restaurant.di

import org.koin.dsl.module
import com.angus.service_restaurant.domain.usecase.*
import com.angus.service_restaurant.domain.usecase.validation.*
import com.angus.service_restaurant.domain.utils.IValidation
import com.angus.service_restaurant.domain.utils.Validation

val UseCasesModule = module {
    single<IValidation> { Validation() }
    single<ICategoryValidationUseCase> { CategoryValidationUseCase(get()) }
    single<IRestaurantValidationUseCase> { RestaurantValidationUseCase(get()) }
    single<IControlRestaurantsUseCase> { ControlRestaurantsUseCase(get(), get(), get()) }
    single<IDiscoverRestaurantUseCase> { DiscoverRestaurantUseCase(get(), get(), get()) }
    single<IManageCategoryUseCase> { ManageCategoryUseCase(get(), get(), get()) }
    single<IManageCuisineUseCase> { ManageCuisineUseCase(get(), get()) }
    single<IMealValidationUseCase> { MealValidationUseCase(get()) }
    single<IManageMealUseCase> { ManageMealUseCase(get(), get(), get(), get()) }
    single<IManageRestaurantDetailsUseCase> { ManageRestaurantDetailsUseCase(get(), get(), get(), get()) }
    single<IManageRestaurantRequestUseCase> { ManageRestaurantRequestUseCase(get(), get()) }
    single<IMangeCartUseCase> { MangeCartUseCase(get(), get(), get()) }
    single<ICartValidationUseCase> { CartValidationUseCase(get()) }
    single<ISearchUseCase> { SearchUseCase(get()) }
    factory<IOrderValidationUseCase> { OrderValidationUseCase(get()) }
    factory<IManageOrderUseCase> { ManageOrderUseCase(get(), get(), get()) }
}