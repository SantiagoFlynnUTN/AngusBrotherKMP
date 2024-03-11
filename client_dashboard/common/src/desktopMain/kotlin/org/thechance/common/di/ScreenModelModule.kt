package di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.angus.common.presentation.app.AppScreenModel
import com.angus.common.presentation.login.LoginScreenModel
import com.angus.common.presentation.main.MainScreenModel
import com.angus.common.presentation.overview.OverviewScreenModel
import com.angus.common.presentation.restaurant.RestaurantScreenModel
import com.angus.common.presentation.taxi.TaxiScreenModel
import com.angus.common.presentation.users.UserScreenModel

val ScreenModelModule = module {
    factoryOf(::OverviewScreenModel)
    factoryOf(::RestaurantScreenModel)
    factoryOf(::TaxiScreenModel)
    factoryOf(::MainScreenModel)
    factoryOf(::UserScreenModel)
    factoryOf(::LoginScreenModel)
    factoryOf(::AppScreenModel)
}