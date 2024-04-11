package di
import org.koin.core.module.dsl.*
import org.koin.dsl.module
import com.angus.common.data.gateway.local.UserLocalGateway
import com.angus.common.data.gateway.fake.FakeRemoteGateway
import com.angus.common.data.gateway.fake.UsersFakeGateway
import com.angus.common.data.gateway.fake.TaxisFakeGateway
import com.angus.common.data.gateway.fake.RestaurantFakeGateway
import com.angus.common.data.gateway.remote.LocationGateway
import com.angus.common.data.gateway.remote.UsersGateway
import com.angus.common.data.gateway.remote.TaxisGateway
import com.angus.common.data.gateway.remote.RestaurantGateway
import com.angus.common.domain.getway.ILocationGateway
import com.angus.common.domain.getway.IRemoteGateway
import com.angus.common.domain.getway.IRestaurantGateway
import com.angus.common.domain.getway.ITaxisGateway
import com.angus.common.domain.getway.IUserLocalGateway
import com.angus.common.domain.getway.IUsersGateway


val GatewayModule = module {
    // region Real Gateways
    singleOf(::UserLocalGateway) { bind<IUserLocalGateway>() }
    singleOf(::UsersGateway) { bind<IUsersGateway>() }
    singleOf(::TaxisGateway) { bind<ITaxisGateway>() }
    singleOf(::RestaurantGateway) { bind<IRestaurantGateway>() }
    singleOf(::LocationGateway) { bind<ILocationGateway>() }
    // endregion

    // region Fake Gateways
    singleOf(::FakeRemoteGateway) { bind<IRemoteGateway>() }
    //singleOf(::UsersFakeGateway) { bind<IUsersGateway>()}
    //singleOf(::TaxisFakeGateway) { bind<ITaxisGateway>() }
    //singleOf(::RestaurantFakeGateway) { bind<IRestaurantGateway>() }

    // endregion
}