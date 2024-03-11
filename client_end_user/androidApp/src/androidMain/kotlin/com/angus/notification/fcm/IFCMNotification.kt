package com.angus.notification.fcm

import android.app.PendingIntent

interface IFCMNotification {
    fun getClickPendingIntent(): PendingIntent
}