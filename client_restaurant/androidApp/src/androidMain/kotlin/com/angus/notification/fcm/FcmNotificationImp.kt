package com.angus.notification.fcm

import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.angus.MainActivity

class FcmNotificationImp(private val context: Context) : IFcmNotification {

    override fun getClickPendingIntent(): PendingIntent {
        val clickIntent = Intent(
            Intent.ACTION_VIEW,
            USER_NOTIFICATION_URI.toUri(),
            context,
            MainActivity::class.java
        )
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(clickIntent)
            getPendingIntent(1, flag)
        }
        return pendingIntent
    }

    companion object {
        const val USER_NOTIFICATION_URI = "http://192.168.0.215:8080/"
        private const val flag = PendingIntent.FLAG_IMMUTABLE
    }
}