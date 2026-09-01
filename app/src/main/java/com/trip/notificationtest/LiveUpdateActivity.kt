package com.trip.notificationtest

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.trip.notificationtest.manager.PushNotificationManager
import com.trip.notificationtest.manager.PushNotificationManager.LiveUpdateStep
import com.trip.notificationtest.ui.TextWithButton
import com.trip.notificationtest.ui.theme.NotificationTestTheme

class LiveUpdateActivity : ComponentActivity() {
    private val pushManager by lazy { PushNotificationManager(this@LiveUpdateActivity) }
    private val tag = javaClass.simpleName

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
                        TextWithButton(
                            "진행 상황 중심 알림(Progress) - 실행"
                        ) {
                            if (Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.BAKLAVA_1) {
                                pushManager.sendNotificationProgress("short!")
                            } else {
                                Toast.makeText(
                                    this@LiveUpdateActivity,
                                    "현재 디바이스 Android Target버전 36 미만입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        TextWithButton(
                            "Live Update - 주문 완료 (0%)"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                pushManager.sendLiveUpdateNotification(LiveUpdateStep.ORDER_PLACED)
                            } else {
                                Toast.makeText(
                                    this@LiveUpdateActivity,
                                    "Android 16(BAKLAVA) 미만입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        TextWithButton(
                            "Live Update - 준비 중 (25%)"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                pushManager.sendLiveUpdateNotification(LiveUpdateStep.PREPARING)
                            } else {
                                Toast.makeText(
                                    this@LiveUpdateActivity,
                                    "Android 16(BAKLAVA) 미만입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        TextWithButton(
                            "Live Update - 배달 출발 (50%)"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                pushManager.sendLiveUpdateNotification(LiveUpdateStep.EN_ROUTE)
                            } else {
                                Toast.makeText(
                                    this@LiveUpdateActivity,
                                    "Android 16(BAKLAVA) 미만입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        TextWithButton(
                            "Live Update - 거의 도착 (75%)"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                pushManager.sendLiveUpdateNotification(LiveUpdateStep.ARRIVING)
                            } else {
                                Toast.makeText(
                                    this@LiveUpdateActivity,
                                    "Android 16(BAKLAVA) 미만입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        TextWithButton(
                            "Live Update - 배달 완료 (100%)"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                pushManager.sendLiveUpdateNotification(LiveUpdateStep.DELIVERED)
                            } else {
                                Toast.makeText(
                                    this@LiveUpdateActivity,
                                    "Android 16(BAKLAVA) 미만입니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        TextWithButton(
                            "Live Update - 알림 취소"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                pushManager.cancelLiveUpdateNotification()
                            }
                        }

                        TextWithButton(
                            "Live Update - Promoted 권한 설정"
                        ) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
                                val canPost = pushManager.canPostPromotedNotifications()
                                if (!canPost) {
                                    val intent = Intent(
                                        android.provider.Settings.ACTION_APP_NOTIFICATION_PROMOTION_SETTINGS
                                    ).apply {
                                        putExtra(
                                            android.provider.Settings.EXTRA_APP_PACKAGE,
                                            packageName
                                        )
                                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    if (intent.resolveActivity(packageManager) != null) {
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(
                                            this@LiveUpdateActivity,
                                            "Promoted notifications 설정 화면을 찾을 수 없습니다.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        this@LiveUpdateActivity,
                                        "이미 Promoted notifications 권한이 활성화되어 있습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}