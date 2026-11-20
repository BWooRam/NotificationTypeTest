package com.hyundaiht.fcmtest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat

class CallActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val notificationId = intent.getIntExtra("notification_id", -1)
        val action = intent.action
        Log.d(TAG, "CallAction received: action=$action, notificationId=$notificationId")

        when (action) {
            ACTION_ANSWER -> {
                Log.d(TAG, "전화 수락")
                // 수락 후 통화 중 알림으로 전환
                val pushManager = com.hyundaiht.fcmtest.manager.PushNotificationManager(context)
                pushManager.showOngoingCallNotification(
                    callerName = "통화 중",
                    notificationId = notificationId
                )
            }
            ACTION_DECLINE -> {
                Log.d(TAG, "전화 거절")
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
            ACTION_HANG_UP -> {
                Log.d(TAG, "통화 종료")
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
            ACTION_BLOCK -> {
                Log.d(TAG, "전화 차단")
                if (notificationId != -1) {
                    NotificationManagerCompat.from(context).cancel(notificationId)
                }
            }
        }
    }

    companion object {
        const val TAG = "CallActionReceiver"
        const val ACTION_ANSWER = "com.hyundaiht.fcmtest.ACTION_ANSWER"
        const val ACTION_DECLINE = "com.hyundaiht.fcmtest.ACTION_DECLINE"
        const val ACTION_HANG_UP = "com.hyundaiht.fcmtest.ACTION_HANG_UP"
        const val ACTION_BLOCK = "com.hyundaiht.fcmtest.ACTION_BLOCK"
    }
}