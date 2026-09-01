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
import com.trip.notificationtest.manager.PushNotificationManager
import com.trip.notificationtest.ui.TextWithButton
import com.trip.notificationtest.ui.theme.NotificationTestTheme
import kotlin.random.Random

class BubbleNotificationActivity : ComponentActivity() {
    private val pushManager by lazy { PushNotificationManager(this@BubbleNotificationActivity) }

    private val aliceMessages = listOf(
        "안녕하세요! 오늘 어떻게 지내셨나요?",
        "주말에 같이 영화 볼래요?",
        "방금 점심 먹었는데 정말 맛있었어요!",
        "프로젝트 피드백 공유드립니다. 시간 되실 때 확인 부탁드려요.",
        "오늘 날씨 정말 좋네요. 산책할까요?",
        "이거 보셨어요? ㅋㅋㅋ 너무 웃겨요",
        "내일 회의 몇 시인지 다시 한 번 알려주실 수 있나요?",
        "감사합니다! 덕분에 잘 해결됐어요.",
        "혹시 주말에 시간 괜찮으세요? 약속 잡고 싶어서요.",
        "좋은 아이디어네요! 한번 적용해보겠습니다."
    )

    private val bobMessages = listOf(
        "회의 자료 공유드립니다.",
        "코드 리뷰 완료했습니다. PR 확인 부탁드려요.",
        "점심 같이 먹을 사람? 12시에 로비에서 만나요.",
        "이슈 티켓 생성했습니다. 링크 확인해주세요.",
        "배포 완료되었습니다. 정상 동작 확인했습니다.",
        "신규 API 스펙 문서 업데이트했습니다.",
        "주말에 푹 쉬세요! 월요일에 뵙겠습니다.",
        "QA 팀에서 버그 리포트가 왔습니다. 확인 부탁드립니다.",
        "오늘 야근 하시나요? 같이 저녁 먹어요.",
        "좋은 정보 감사합니다! 바로 적용해볼게요."
    )

    private val alexMessages = listOf(
        "안녕! 오랜만이네요. 잘 지내고 있죠?",
        "아까 보낸 파일 확인해봤어? 이상한 점 있으면 알려줘.",
        "다음 주 출장 일정 확정됐어. 화요일에 출발이야.",
        "이번 주 금요일 팀 회의 취소됐어. 일정 다시 잡을게.",
        "새로운 프로젝트 착수회의 내일 오전 10시야. 늦지 마!",
        "서버 이전 작업 완료됐어. 이제 속도 훨씬 빨라질 거야.",
        "디자인 시안 올렸어. 피드백 주면 수정할게.",
        "고생했어! 결과물 정말 좋네요.",
        "다음 주에 리뷰 미팅 잡아둘게. 시간 되는지 알려줘.",
        "이번 분기 목표 달성 축하해! 팀원들 다 고생했어."
    )

    private val contacts = listOf("Alice" to aliceMessages, "Bob" to bobMessages, "Alex" to alexMessages)

    private fun getRandomMessage(messages: List<String>): String {
        return messages[Random.nextInt(messages.size)]
    }

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
                            "버블 알림 (채팅 - Alice 랜덤) - 실행"
                        ) {
                            pushManager.sendBubbleNotification(
                                chatName = "Alice",
                                message = getRandomMessage(aliceMessages)
                            )
                        }

                        TextWithButton(
                            "버블 알림 (채팅 - Bob 랜덤) - 실행"
                        ) {
                            pushManager.sendBubbleNotification(
                                chatName = "Bob",
                                message = getRandomMessage(bobMessages)
                            )
                        }

                        TextWithButton(
                            "버블 알림 (채팅 - Alex 랜덤) - 실행"
                        ) {
                            pushManager.sendBubbleNotification(
                                chatName = "Alex",
                                message = getRandomMessage(alexMessages)
                            )
                        }

                        TextWithButton(
                            "버블 알림 (전체 랜덤) - 실행"
                        ) {
                            val (name, messages) = contacts[Random.nextInt(contacts.size)]
                            pushManager.sendBubbleNotification(
                                chatName = name,
                                message = getRandomMessage(messages)
                            )
                        }

                        TextWithButton(
                            "버블 알림 (자동 펼침 + 알림 숨김) - 실행"
                        ) {
                            val (name, messages) = contacts[Random.nextInt(contacts.size)]
                            pushManager.sendBubbleNotification(
                                chatName = name,
                                message = getRandomMessage(messages),
                                autoExpand = true,
                                suppressNotification = true
                            )
                        }
                    }
                }
            }
        }
    }
}