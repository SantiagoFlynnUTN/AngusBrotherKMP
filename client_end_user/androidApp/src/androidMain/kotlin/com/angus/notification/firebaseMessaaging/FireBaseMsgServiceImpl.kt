package com.angus.notification.firebaseMessaaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.angus.client.R
import com.angus.notification.fcm.IFCMNotification
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import data.gateway.service.IFireBaseMessageService
import domain.gateway.local.ILocalConfigurationGateway
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FireBaseMsgServiceImpl() : FirebaseMessagingService(), IFireBaseMessageService {
    private val fcmNotification: IFCMNotification by inject()
    private val localConfigGateway: ILocalConfigurationGateway by inject()


    override fun onNewToken(token: String) {
        Log.d("TAG", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val notification = remoteMessage.notification
        CoroutineScope(Dispatchers.IO).launch {
            if (notification != null) {
                val title = notification.title
                val body = notification.body + localConfigGateway.getAccessToken()
                showNotification(title, body)
            }
        }
    }

    private fun showNotification(title: String?, message: String?) {
        val clickPendingIntent = fcmNotification.getClickPendingIntent()
        val channelId = "default_channel"
        val notificationId = 1
        val builder = NotificationCompat.Builder(this, channelId).apply {
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setContentTitle(title)
            setContentText(message)
            priority = NotificationCompat.PRIORITY_HIGH
            addAction(0, "Show", clickPendingIntent)
        }

        clickPendingIntent?.let {
            builder.setContentIntent(it)
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(notificationId, builder.build())
    }

    override suspend fun getDeviceToken(): String {
        return suspendCoroutine {
            FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                it.resume(token)
            }.addOnFailureListener { exception ->
                it.resumeWithException(exception)
            }
        }
    }
}