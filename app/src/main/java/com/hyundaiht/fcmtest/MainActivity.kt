package com.hyundaiht.fcmtest

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.hyundaiht.fcmtest.manager.PushNotificationManager
import com.hyundaiht.fcmtest.ui.TextWithButton
import com.hyundaiht.fcmtest.ui.theme.NotificationTestTheme

class MainActivity : ComponentActivity() {
    private val pushManager by lazy { PushNotificationManager(this@MainActivity) }
    private val tag = javaClass.simpleName

    private var fcmToken by mutableStateOf<String?>(null)
    private var isFetchingToken by mutableStateOf(false)

    override fun onStart() {
        super.onStart()
        fetchFcmToken()
    }

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
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "Fcm Token",
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                modifier = Modifier.padding(top = 4.dp),
                                text = when {
                                    isFetchingToken -> "토큰을 가져오는 중…"
                                    else -> fcmToken ?: "토큰을 가져오지 못했습니다. 갱신 버튼을 눌러주세요."
                                },
                                fontSize = 12.sp
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Button(
                                    onClick = { refreshFcmToken() },
                                    enabled = !isFetchingToken
                                ) {
                                    Text("갱신")
                                }
                                Button(
                                    onClick = { copyFcmTokenToClipboard() },
                                    enabled = !fcmToken.isNullOrBlank()
                                ) {
                                    Text("복사")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

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

    private fun fetchFcmToken() {
        isFetchingToken = true
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            isFetchingToken = false
            if (!task.isSuccessful) {
                fcmToken = null
                Log.d(tag, "Fetching FCM registration token failed: ${task.exception}")
                return@addOnCompleteListener
            }
            fcmToken = task.result
            Log.d(tag, "Fetching FCM registration token = ${task.result}")
        }
    }

    private fun refreshFcmToken() {
        Log.d(tag, "Refreshing FCM registration token")
        FirebaseMessaging.getInstance().deleteToken().addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d(tag, "Deleting FCM registration token failed: ${task.exception}")
            }
            fetchFcmToken()
        }
    }

    private fun copyFcmTokenToClipboard() {
        val token = fcmToken
        if (token.isNullOrBlank()) {
            Toast.makeText(this, "복사할 토큰이 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("FCM Token", token))
        Toast.makeText(this, "토큰이 복사되었습니다.", Toast.LENGTH_SHORT).show()
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