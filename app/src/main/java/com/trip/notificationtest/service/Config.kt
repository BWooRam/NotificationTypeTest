package com.trip.notificationtest.service

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.trip.notificationtest.MainActivity
import com.trip.notificationtest.R

object Config {
    var isSound = false

    const val notificationID = "haeroad_service_channel"
    const val NOTI_CHANNEL_ID_DEFAULT = "NOTI_CHANNEL_ID_DEFAULT"
    const val NOTI_CHANNEL_NAME_DEFAULT = "NOTI_CHANNEL_NAME_DEFAULT"
    const val NOTI_ID_DEFAULT = 1000


    fun getCallAcceptIntent(context: Context): PendingIntent {
        val customNotificationId = 1000 // Q버전 이상에서 사용하는 custom notification id.
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(customNotificationId)

        val notificationActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtras(Bundle().apply { putBoolean("getCallAcceptIntent", true) })
            putExtra("getCallAcceptIntent", true)
        }

        val resultPendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(notificationActivityIntent)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        return resultPendingIntent
    }

    fun getCallIntent(context: Context): PendingIntent {
        val customNotificationId = 1000 // Q버전 이상에서 사용하는 custom notification id.
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(customNotificationId)

        val notificationActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtras(Bundle().apply { putBoolean("getCallIntent", true) })
            putExtra("getCallIntent", true)
        }

        val resultPendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(notificationActivityIntent)
            .getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        return resultPendingIntent
    }

    fun getCancelIntent(context: Context): PendingIntent {
        val customNotificationId = NOTI_ID_DEFAULT // Q버전 이상에서 사용하는 custom notification id.
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(customNotificationId)

        val notificationActivityIntent = Intent(context, MainActivity::class.java).apply {
            putExtras(Bundle().apply { putBoolean("getCancelIntent", true) })
            putExtra("getCancelIntent", true)
        }

        val resultPendingIntent = TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(notificationActivityIntent)
            .getPendingIntent(2, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE)

        return resultPendingIntent
    }

}