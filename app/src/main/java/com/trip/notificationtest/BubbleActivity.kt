package com.trip.notificationtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.trip.notificationtest.manager.PushNotificationManager
import com.trip.notificationtest.manager.ChatMessage
import com.trip.notificationtest.ui.theme.NotificationTestTheme

class BubbleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val chatName = intent?.getStringExtra("chat_name") ?: "Unknown"

        setContent {
            NotificationTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BubbleContent(
                        chatName = chatName,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
private fun BubbleContent(
    chatName: String,
    modifier: Modifier = Modifier
) {
    val messages = PushNotificationManager.chatHistory[chatName] ?: remember { mutableStateListOf() }
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = chatName,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { msg ->
                MessageBubble(msg, screenWidth)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("메시지 입력", fontSize = 14.sp) },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            TextButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        PushNotificationManager.addMyMessageToHistory(chatName, inputText.trim())
                        inputText = ""
                    }
                }
            ) {
                Text("전송")
            }
        }
    }
}

@Composable
private fun MessageBubble(msg: ChatMessage, screenWidth: androidx.compose.ui.unit.Dp) {
    val maxBubbleWidth = screenWidth * 0.75f

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (msg.isMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (msg.isMe) Color(0xFFDCF8C6) else Color(0xFFE3F2FD),
            modifier = Modifier.widthIn(max = maxBubbleWidth)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (!msg.isMe) {
                    Text(
                        text = "상대방",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = msg.text,
                    fontSize = 15.sp
                )
            }
        }
    }
}