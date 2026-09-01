package com.trip.notificationtest

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.trip.notificationtest.manager.PushNotificationManager
import com.trip.notificationtest.ui.TextWithButton
import com.trip.notificationtest.ui.theme.NotificationTestTheme

class CallStyleNotificationActivity : ComponentActivity() {
    private val pushManager by lazy { PushNotificationManager(this) }

    private val callers = listOf("김현대", "이삼성", "박엘지", "최한화", "정SK")
    private val callId = 6001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pushManager.createNotificationChannel()
        enableEdgeToEdge()
        setContent {
            NotificationTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        TextWithButton("수신 전화 알림 (Incoming Call) - 실행") {
                            val caller = callers.random()
                            pushManager.showIncomingCallNotification(
                                callerName = caller,
                                notificationId = callId
                            )
                        }

                        TextWithButton("진행 중 통화 알림 (Ongoing Call) - 실행") {
                            val caller = callers.random()
                            pushManager.showOngoingCallNotification(
                                callerName = caller,
                                notificationId = callId + 1
                            )
                        }

                        TextWithButton("스크리닝 전화 알림 (Screening Call) - 실행") {
                            val caller = callers.random()
                            pushManager.showScreeningCallNotification(
                                callerName = caller,
                                notificationId = callId + 2
                            )
                        }

                        TextWithButton("통화 알림 취소 - 실행") {
                            pushManager.cancelCallNotification(callId)
                            pushManager.cancelCallNotification(callId + 1)
                            pushManager.cancelCallNotification(callId + 2)
                        }
                    }
                }
            }
        }
    }
}