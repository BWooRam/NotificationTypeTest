package com.trip.notificationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import com.trip.notificationtest.manager.PushNotificationManager
import com.trip.notificationtest.ui.TextWithButton
import com.trip.notificationtest.ui.theme.NotificationTestTheme

class ExpandedNotificationActivity : ComponentActivity() {
    private val pushManager by lazy { PushNotificationManager(this@ExpandedNotificationActivity) }

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
                            "기본 알림 메시지 - 실행"
                        ) {
                            pushManager.sendNotification(
                                title = "sendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotificationsendNotification",
                                body = "bodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybodybody"
                            )
                        }

                        TextWithButton(
                            "알림 그룹 메시지1 - 실행"
                        ) {
                            pushManager.sendGroupNotification(
                                "sendGroupNotification test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1test1",
                                "sendGroupNotification test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2test2"
                            )
                        }

                        TextWithButton(
                            "알림 그룹 메시지2 - 실행"
                        ) {
                            pushManager.sendGroupNotification(
                                "sendGroupNotification test2",
                                "sendGroupNotification test2",
                                groupKey = "com.example.group_key2",
                                notificationId = 999997
                            )
                        }

                        TextWithButton(
                            "확장형 알림(Big Picture Icon O) - 실행"
                        ) {
                            pushManager.sendExpandedNotificationBigPicture1(
                                "sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1",
                                "sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2",
                                true
                            )
                        }

                        TextWithButton(
                            "확장형 알림(Big Picture Icon X) - 실행"
                        ) {
                            pushManager.sendExpandedNotificationBigPicture1(
                                "sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1sendExpandedNotificationBigPicture1",
                                "sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2sendExpandedNotificationBigPicture2",
                                false
                            )
                        }

                        TextWithButton(
                            "확장형 알림(Big Text) - 실행"
                        ) {
                            pushManager.sendExpandedNotificationBigText(
                                "titletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitletitle",
                                "contentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontentcontent",
                                "긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트긴 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트 텍스트"
                            )
                        }

                        TextWithButton(
                            "받은편지함 스타일 알림(Inbox FullText O) - 실행"
                        ) {
                            pushManager.sendNotificationInbox(
                                "sendNotificationInbox test",
                                "sendNotificationInbox test",
                                true
                            )
                        }

                        TextWithButton(
                            "받은편지함 스타일 알림(Inbox FullText X) - 실행"
                        ) {
                            pushManager.sendNotificationInbox(
                                "sendNotificationInbox test",
                                "sendNotificationInbox test",
                                false
                            )
                        }

                        TextWithButton(
                            "대화 표시 알림(Messaging) - 실행"
                        ) {
                            pushManager.sendNotificationMessaging(
                                NotificationCompat.MessagingStyle.Message(
                                    "Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1Message1",
                                    System.currentTimeMillis(),
                                    Person.Builder().setName("YouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYouYou").build()
                                ),
                                NotificationCompat.MessagingStyle.Message(
                                    "Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2Message2",
                                    System.currentTimeMillis(),
                                    Person.Builder().setName("MeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMeMe").build()
                                )
                            )
                        }

                        TextWithButton(
                            "미디어 컨트롤 알림(Media) - 실행"
                        ) {
                            pushManager.sendNotificationMedia()
                        }
                    }
                }
            }
        }
    }
}