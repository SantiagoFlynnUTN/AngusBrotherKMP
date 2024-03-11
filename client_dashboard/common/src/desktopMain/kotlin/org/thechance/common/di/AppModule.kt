package di

import org.koin.dsl.module

fun appModule() = module {
    includes(
        LocalStorageModule,
        UseCaseModule,
        GatewayModule,
        ScreenModelModule,
        NetworkModule
    )
}
