package com.angus.service_location.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import com.angus.service_location.di.appModule


fun Application.configureDependencyInjection() {
  install(Koin){
      modules(appModule)
  }
}