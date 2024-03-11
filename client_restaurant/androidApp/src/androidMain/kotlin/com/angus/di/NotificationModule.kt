package com.angus.di

import com.angus.notification.FirebaseMessagingServiceImp
import com.angus.notification.fcm.FcmNotificationImp
import com.angus.notification.fcm.IFcmNotification
import data.service.IFirebaseMessagingService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule = module {
    singleOf(::FirebaseMessagingServiceImp) { bind<IFirebaseMessagingService>() }
    single<IFcmNotification> { FcmNotificationImp(androidContext().applicationContext) }
}