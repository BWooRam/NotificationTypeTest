package com.trip.notificationtest.manager

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Icon
import android.media.MediaExtractor
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.trip.notificationtest.BubbleActivity
import com.trip.notificationtest.CallActionReceiver
import com.trip.notificationtest.CallStyleNotificationActivity
import com.trip.notificationtest.R
import java.io.InputStream
import java.util.UUID
import kotlin.random.Random
import androidx.core.graphics.toColorInt
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList

data class ChatMessage(val text: String, val timestamp: Long, val isMe: Boolean)

class PushNotificationManager(
    private val context: Context
) {
    companion object {
        const val NOTI_CHANNEL_ID_DEFAULT = "HT-HOME-INTEGRATED-NOTI"
        const val NOTI_CHANNEL_NAME_DEFAULT = "HT-HOME-INTEGRATED-NOTI"
        const val GROUP_KEY = "com.trip.notificationtest.Test"
        const val LIVE_UPDATE_NOTIFICATION_ID = 5001

        val chatHistory = mutableMapOf<String, SnapshotStateList<ChatMessage>>()

        fun addMyMessageToHistory(chatName: String, message: String) {
            val history = chatHistory.getOrPut(chatName) { mutableStateListOf() }
            history.add(ChatMessage(message, System.currentTimeMillis(), isMe = true))
            if (history.size > 50) {
                history.removeAt(0)
            }
        }
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as android.app.NotificationManager

    fun createNotificationChannel() {
        //Channel ?앹꽦
        if (notificationManager.getNotificationChannel(NOTI_CHANNEL_ID_DEFAULT) == null) {
            val createChannel = NotificationChannel(
                NOTI_CHANNEL_ID_DEFAULT,
                NOTI_CHANNEL_NAME_DEFAULT,
                android.app.NotificationManager.IMPORTANCE_HIGH
            )
            createChannel.setAllowBubbles(true)
            notificationManager.createNotificationChannel(createChannel)
        } else {
            Log.i(javaClass.simpleName, "createNotificationChannel not create")
        }
    }

    fun sendNotification(title: String, body: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        //Notification ?앹꽦
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
                .also { noti ->
                    noti.setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(false)
                        .setVibrate(longArrayOf(0, 1000))
                        .priority = NotificationCompat.PRIORITY_HIGH
                }

        val id = UUID.randomUUID().hashCode()
        notificationManager.notify(id, notificationBuilder.build())
    }

    fun sendGroupNotification(
        title: String,
        content: String,
        groupKey: String = GROUP_KEY,
        notificationId: Int = 99999
    ) {
        val newMessageNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setGroup(groupKey)
            .build()

        val summaryNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setContentTitle(title)
            // Set content text to support devices running API level < 24.
            .setContentText(content)
            .setStyle(NotificationCompat.InboxStyle().setSummaryText("test test"))
            .setSmallIcon(R.drawable.ic_launcher_background)
            // Build summary info into InboxStyle template.
            // Specify which group this notification belongs to.
            .setGroup(groupKey)
            // Set this notification as the summary for the group.
            .setGroupSummary(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, newMessageNotification)
        NotificationManagerCompat.from(context).notify(notificationId, summaryNotification)
    }

    fun sendExpandedNotificationBigPicture1(
        title: String,
        content: String,
        isIcon: Boolean
    ) {
        val bitmapImage = getBitmapFromAssets(context, "image.jpg")
        val style = if (isIcon) {
            NotificationCompat.BigPictureStyle()
                .bigPicture(bitmapImage)
                .bigLargeIcon(bitmapImage)
        } else {
            NotificationCompat.BigPictureStyle()
                .bigPicture(bitmapImage)
                .bigLargeIcon(null as Bitmap?)
        }
        val newMessageNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(style)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, newMessageNotification)
    }

    private fun getBitmapFromAssets(context: Context, fileName: String): Bitmap? {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream.close()
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun sendExpandedNotificationBigText(
        title: String,
        content: String,
        longContent: String
    ) {
        val newMessageNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setLargeIcon(Icon.createWithResource(context, R.drawable.ic_launcher_background))
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(longContent)
            )
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, newMessageNotification)
    }

    fun sendNotificationInbox(
        title: String,
        content: String,
        isFull: Boolean
    ) {
        val style = if (isFull) {
            NotificationCompat.InboxStyle()
                .addLine("Inbox 1Inbox 1Inbox 1Inbox 1Inbox 1Inbox 1Inbox 1Inbox 1Inbox 1")
                .addLine("Inbox 2Inbox 2Inbox 2Inbox 2Inbox 2Inbox 2Inbox 2Inbox 2Inbox 2")
                .addLine("Inbox 3Inbox 3Inbox 3Inbox 3Inbox 3Inbox 3Inbox 3Inbox 3Inbox 3")
                .addLine("Inbox 4Inbox 4Inbox 4Inbox 4Inbox 4Inbox 4Inbox 4Inbox 4Inbox 4")
                .addLine("Inbox 5Inbox 5Inbox 5Inbox 5Inbox 5Inbox 5Inbox 5Inbox 5Inbox 5")
                .addLine("Inbox 6Inbox 6Inbox 6Inbox 6Inbox 6Inbox 6Inbox 6Inbox 6Inbox 6")
                .addLine("Inbox 7Inbox 7Inbox 7Inbox 7Inbox 7Inbox 7Inbox 7Inbox 7Inbox 7")
        } else {
            NotificationCompat.InboxStyle()
                .addLine("Inbox 1")
                .addLine("Inbox 2")
                .addLine("Inbox 3")
        }
        val newMessageNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(style)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, newMessageNotification)
    }

    fun sendNotificationMessaging(
        message1: NotificationCompat.MessagingStyle.Message,
        message2: NotificationCompat.MessagingStyle.Message
    ) {
        val newMessageNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setStyle(
                NotificationCompat.MessagingStyle(Person.Builder().setName("Person").build())
                    .setConversationTitle("ConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitleConversationTitle")
                    .addMessage(message1)
                    .addMessage(message2)
            )
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, newMessageNotification)
    }

    fun sendNotificationMedia() {
        val newMessageNotification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .addAction(
                R.drawable.baseline_arrow_back_ios_24,
                "Previous",
                null /* Add valid intent */
            ) // #0
            .addAction(
                R.drawable.baseline_arrow_forward_ios_24,
                "Pause",
                null /* Add valid intent */
            ) // #1
            .addAction(
                R.drawable.outline_power_settings_new_24,
                "Next",
                null /* Add valid intent */
            ) // #2
            .setContentTitle("Wonderful music")
            .setContentText("My Awesome Band")
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        val id = UUID.randomUUID().hashCode()
        NotificationManagerCompat.from(context).notify(id, newMessageNotification)
    }

    @RequiresApi(Build.VERSION_CODES_FULL.BAKLAVA_1)
    fun sendNotificationProgress(shortText: String) {
        val newMessageNotification = Notification.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setRequestPromotedOngoing(true)
            .setStyle(getProgressStyle())
            .setShortCriticalText(shortText)
            .addAction(
                R.drawable.baseline_arrow_back_ios_24,
                "Action1",
                null /* Add valid intent */
            ) // #0
            .addAction(
                R.drawable.baseline_arrow_forward_ios_24,
                "Action2",
                null /* Add valid intent */
            ) // #1
            .setContentTitle("Wonderful music")
            .setContentText("My Awesome Band")
            .build()

        val hasPromotableCharacteristics = newMessageNotification.hasPromotableCharacteristics()
        Log.d(
            "sendNotificationProgress",
            "hasPromotableCharacteristics = $hasPromotableCharacteristics"
        )

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        NotificationManagerCompat.from(context).notify(1, newMessageNotification)
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun getProgressStyle(): Notification.ProgressStyle {
        val progress = Random.nextInt(0, 600)
        val range1 = Random.nextInt(0, 50)
        val range2 = Random.nextInt(500, 600)
        val range3 = Random.nextInt(200, 300)
        val range4 = Random.nextInt(50, 100)
        val point1 = Random.nextInt(50, 100)
        val point2 = Random.nextInt(50, 100)
        return Notification.ProgressStyle()
            .setStyledByProgress(false)
            .setProgress(progress)
            .setProgressTrackerIcon(
                Icon.createWithResource(
                    context,
                    R.drawable.baseline_directions_car_24
                )
            )
            .setProgressSegments(
                listOf(
                    Notification.ProgressStyle.Segment(range1).setColor(Color.BLACK),
                    Notification.ProgressStyle.Segment(range2).setColor(Color.YELLOW),
                    Notification.ProgressStyle.Segment(range3).setColor(Color.WHITE),
                    Notification.ProgressStyle.Segment(range4).setColor(Color.BLUE)
                )
            )
            .setProgressPoints(
                listOf(
                    Notification.ProgressStyle.Point(point1).setColor(Color.RED),
                    Notification.ProgressStyle.Point(point2).setColor(Color.GREEN)
                )
            )
    }
    /*fun sendRemoteNotification(
        remoteView: NotificationManager.RemoteView,
        notification: NotificationManager.Notification
    ) {
        //Notification 諛?RemoteView Meta 而⑤쾭??
        val notificationMeta = runCatching {
            notification as? Notification
        }.getOrNull()

        val remoteViewMeta = runCatching {
            remoteView as? RemoteView
        }.getOrNull()

        //Notification 諛?RemoteView Meta 泥댄겕
        if (notificationMeta == null) {
            Log.e(javaClass.simpleName, "sendNotification Notification is null")
            return
        }

        if (remoteViewMeta == null) {
            Log.e(javaClass.simpleName, "sendNotification remoteViewMeta is null")
            return
        }

        //RemoteView Notification ?앹꽦
        val notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, notificationMeta.channelId)
        } else {
            NotificationCompat.Builder(context)
        }.also {
            it.setWhen(System.currentTimeMillis())
                //Set the time that the event occurred. Notifications in the panel are sorted by this time.
                .setSmallIcon(notificationMeta.smallIcon)
                .setCustomContentView(remoteViewMeta.remoteViews)
                .setCustomBigContentView(remoteViewMeta.remoteViews)
                .setAutoCancel(notificationMeta.autoCancel)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(remoteViewMeta.contentIntent)

            if (remoteViewMeta.fullScreenIntent != null)
                it.setFullScreenIntent(remoteViewMeta.fullScreenIntent, true)
        }

        notificationManager.notify(notificationMeta.id, notificationBuilder.build())
    }*/

    enum class LiveUpdateStep(val progress: Int, val title: String, val content: String, val shortText: String) {
        ORDER_PLACED(0, "주문 완료", "매장에서 주문을 확인 중입니다", "주문"),
        PREPARING(25, "준비 중", "음식을 조리하고 있습니다", "조리중"),
        EN_ROUTE(50, "배달 출발", "라이더가 배달을 시작했습니다", "배달중"),
        ARRIVING(75, "거의 도착", "배달지에 가까이 오고 있습니다", "도착임박"),
        DELIVERED(100, "배달 완료", "즐거운 식사 되세요", "완료")
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun canPostPromotedNotifications(): Boolean {
        return notificationManager.canPostPromotedNotifications()
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun sendLiveUpdateNotification(step: LiveUpdateStep) {
        val notification = if (Build.VERSION.SDK_INT_FULL >= Build.VERSION_CODES_FULL.BAKLAVA_1) {
            Notification.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setRequestPromotedOngoing(true)
                .setContentTitle(step.title)
                .setContentText(step.content)
                .setShortCriticalText(step.shortText)
                .setStyle(buildLiveUpdateProgressStyle(step.progress))
                .build()
        } else {
            Notification.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentTitle(step.title)
                .setContentText(step.content)
                .setShortCriticalText(step.shortText)
                .setStyle(buildLiveUpdateProgressStyle(step.progress))
                .build()
        }

        val hasPromotable = notification.hasPromotableCharacteristics()
        val canPost = canPostPromotedNotifications()
        Log.d(
            "LiveUpdate",
            "step=${step.name} progress=${step.progress} hasPromotable=$hasPromotable canPostPromoted=$canPost"
        )

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }
        NotificationManagerCompat.from(context).notify(LIVE_UPDATE_NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    fun cancelLiveUpdateNotification() {
        NotificationManagerCompat.from(context).cancel(LIVE_UPDATE_NOTIFICATION_ID)
    }

    @RequiresApi(Build.VERSION_CODES.BAKLAVA)
    private fun buildLiveUpdateProgressStyle(progress: Int): Notification.ProgressStyle {
        val segments = listOf(
            Notification.ProgressStyle.Segment(25).setColor("#4CAF50".toColorInt()),
            Notification.ProgressStyle.Segment(25).setColor("#FF9800".toColorInt()),
            Notification.ProgressStyle.Segment(25).setColor("#2196F3".toColorInt()),
            Notification.ProgressStyle.Segment(25).setColor("#9C27B0".toColorInt())
        )

        val points = when {
            progress >= 75 -> listOf(
                Notification.ProgressStyle.Point(25).setColor(Color.RED),
                Notification.ProgressStyle.Point(50).setColor(Color.RED),
                Notification.ProgressStyle.Point(75).setColor(Color.RED)
            )
            progress >= 50 -> listOf(
                Notification.ProgressStyle.Point(25).setColor(Color.RED),
                Notification.ProgressStyle.Point(50).setColor(Color.RED)
            )
            progress >= 25 -> listOf(
                Notification.ProgressStyle.Point(25).setColor(Color.RED)
            )
            else -> emptyList()
        }

        return Notification.ProgressStyle()
            .setStyledByProgress(false)
            .setProgress(progress)
            .setProgressTrackerIcon(
                Icon.createWithResource(context, R.drawable.baseline_directions_car_24)
            )
            .setProgressSegments(segments)
            .setProgressPoints(points)
    }

    fun createNotificationChannelGroup(context: Context, groupId: String, groupName: String) {
        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
        notificationManager?.createNotificationChannelGroup(
            NotificationChannelGroup(
                groupId,
                groupName
            )
        )
    }

    

    private fun addMessageToHistory(chatName: String, message: String) {
        val history = chatHistory.getOrPut(chatName) { mutableStateListOf() }
        history.add(ChatMessage(message, System.currentTimeMillis(), isMe = false))
        if (history.size > 50) {
            history.removeAt(0)
        }
    }

    fun sendBubbleNotification(
        chatName: String,
        message: String,
        autoExpand: Boolean = false,
        suppressNotification: Boolean = false
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }

        addMessageToHistory(chatName, message)

        val shortcutId = "bubble_$chatName"

        val chatPartner = Person.Builder()
            .setName(chatName)
            .setImportant(true)
            .build()
        val me = Person.Builder().setName("Me").build()

        val shortcut = ShortcutInfoCompat.Builder(context, shortcutId)
            .setCategories(setOf("com.trip.notificationtest.category.BUBBLE"))
            .setIntent(Intent(Intent.ACTION_DEFAULT))
            .setLongLived(true)
            .setShortLabel(chatName)
            .build()
        ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)

        val bubbleIntent = Intent(context, BubbleActivity::class.java).apply {
            putExtra("chat_name", chatName)
        }
        val pendingBubbleIntent = PendingIntent.getActivity(
            context,
            chatName.hashCode(),
            bubbleIntent,
            PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val bubbleData = NotificationCompat.BubbleMetadata.Builder(
            pendingBubbleIntent,
            IconCompat.createWithResource(context, R.drawable.ic_launcher_foreground)
        )
            .setDesiredHeight(600)
            .apply {
                if (autoExpand) setAutoExpandBubble(true)
                if (suppressNotification) setSuppressNotification(true)
            }
            .build()

        val messagingStyle = NotificationCompat.MessagingStyle(me)
            .setConversationTitle(chatName)

        chatHistory[chatName]?.forEach { msg ->
            val sender = if (msg.isMe) me else chatPartner
            messagingStyle.addMessage(msg.text, msg.timestamp, sender)
        }

        val notification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setStyle(messagingStyle)
            .setBubbleMetadata(bubbleData)
            .setShortcutId(shortcutId)
            .addPerson(chatPartner)
            .setOnlyAlertOnce(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(chatName.hashCode(), notification)
    }

    // ==================== CallStyle Notifications ====================

    fun showIncomingCallNotification(callerName: String, notificationId: Int) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }

        val caller = Person.Builder()
            .setName(callerName)
            .setImportant(true)
            .build()

        val answerIntent = Intent(context, CallActionReceiver::class.java).apply {
            action = CallActionReceiver.ACTION_ANSWER
            putExtra("notification_id", notificationId)
        }
        val declineIntent = Intent(context, CallActionReceiver::class.java).apply {
            action = CallActionReceiver.ACTION_DECLINE
            putExtra("notification_id", notificationId)
        }
        val fullScreenIntent = Intent(context, CallStyleNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        val answerPendingIntent = PendingIntent.getBroadcast(
            context, notificationId * 10 + 1, answerIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val declinePendingIntent = PendingIntent.getBroadcast(
            context, notificationId * 10 + 2, declineIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, notificationId * 10 + 3, fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val callStyle = NotificationCompat.CallStyle.forIncomingCall(
            caller, declinePendingIntent, answerPendingIntent
        )

        val notification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Incoming Call")
            .setContentText("Incoming call from $callerName")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setStyle(callStyle)
            .addPerson(caller)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun showOngoingCallNotification(callerName: String, notificationId: Int) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }

        val caller = Person.Builder()
            .setName(callerName)
            .setImportant(true)
            .build()

        val hangUpIntent = Intent(context, CallActionReceiver::class.java).apply {
            action = CallActionReceiver.ACTION_HANG_UP
            putExtra("notification_id", notificationId)
        }
        val hangUpPendingIntent = PendingIntent.getBroadcast(
            context, notificationId * 10 + 4, hangUpIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val callStyle = NotificationCompat.CallStyle.forOngoingCall(
            caller, hangUpPendingIntent
        )

        val fullScreenIntent = Intent(context, CallStyleNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, notificationId * 10 + 8, fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Ongoing Call")
            .setContentText("On call with $callerName")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setStyle(callStyle)
            .addPerson(caller)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun showScreeningCallNotification(callerName: String, notificationId: Int) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e(javaClass.simpleName, "POST_NOTIFICATIONS not permission")
            return
        }

        val caller = Person.Builder()
            .setName(callerName)
            .setImportant(true)
            .build()

        val answerIntent = Intent(context, CallActionReceiver::class.java).apply {
            action = CallActionReceiver.ACTION_ANSWER
            putExtra("notification_id", notificationId)
        }
        val declineIntent = Intent(context, CallActionReceiver::class.java).apply {
            action = CallActionReceiver.ACTION_DECLINE
            putExtra("notification_id", notificationId)
        }

        val answerPendingIntent = PendingIntent.getBroadcast(
            context, notificationId * 10 + 5, answerIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val declinePendingIntent = PendingIntent.getBroadcast(
            context, notificationId * 10 + 6, declineIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val callStyle = NotificationCompat.CallStyle.forScreeningCall(
            caller, declinePendingIntent, answerPendingIntent
        )

        val fullScreenIntent = Intent(context, CallStyleNotificationActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        val fullScreenPendingIntent = PendingIntent.getActivity(
            context, notificationId * 10 + 9, fullScreenIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, NOTI_CHANNEL_ID_DEFAULT)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Screening Call")
            .setContentText("Screening call from $callerName")
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setStyle(callStyle)
            .addPerson(caller)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun cancelCallNotification(notificationId: Int) {
        NotificationManagerCompat.from(context).cancel(notificationId)
    }
}
