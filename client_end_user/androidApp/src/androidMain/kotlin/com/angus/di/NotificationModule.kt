package com.angus.di

import com.angus.notification.fcm.FCMNotificationImp
import com.angus.notification.firebaseMessaaging.FireBaseMsgServiceImpl
import com.angus.notification.fcm.IFCMNotification
import data.gateway.service.IFireBaseMessageService
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val firebaseModule = module {
    singleOf(::FireBaseMsgServiceImpl) { bind<IFireBaseMessageService>() }
    single<IFCMNotification> { FCMNotificationImp(androidContext().applicationContext) }
}