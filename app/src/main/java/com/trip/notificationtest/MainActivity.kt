package com.trip.notificationtest

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.trip.notificationtest.manager.PushNotificationManager
import com.trip.notificationtest.ui.TextWithButton
import com.trip.notificationtest.ui.theme.NotificationTestTheme

class MainActivity : ComponentActivity() {
    private val pushManager by lazy { PushNotificationManager(this@MainActivity) }
    private val tag = javaClass.simpleName

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                Log.d(tag, "POST_NOTIFICATIONS granted")
            } else {
                Log.d(tag, "POST_NOTIFICATIONS denied")
                Toast.makeText(
                    this@MainActivity,
                    "알림 권한이 거부되어 알림이 표시되지 않습니다.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestNotificationPermissionIfNeeded()
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
                            "Fcm Token 가져오기 - 실행"
                        ) {
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                    if (!task.isSuccessful) {
                                        Log.d(tag, "Fetching FCM registration token failed")
                                        return@OnCompleteListener
                                    }

                                    Log.d(tag, "Fetching FCM registration token = ${task.result}")
                                })
                        }

                        TextWithButton(
                            "펼칠 수 있는 알림 (Group / BigPicture / BigText / Inbox / Messaging / Media) - 실행"
                        ) {
                            val intent = Intent(this@MainActivity, ExpandedNotificationActivity::class.java)
                            startActivity(intent)
                        }

                        TextWithButton(
                            "실시간 업데이트 알림 (Progress / Live Updates) - 실행"
                        ) {
                            val intent = Intent(this@MainActivity, LiveUpdateActivity::class.java)
                            startActivity(intent)
                        }

                        TextWithButton(
                            "버블 알림 (Bubbles) - 실행"
                        ) {
                            val intent = Intent(this@MainActivity, BubbleNotificationActivity::class.java)
                            startActivity(intent)
                        }

                        TextWithButton(
                            "전화 알림 (CallStyle) - 실행"
                        ) {
                            val intent = Intent(this@MainActivity, CallStyleNotificationActivity::class.java)
                            startActivity(intent)
                        }
                    }
                }
            }
        }
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}