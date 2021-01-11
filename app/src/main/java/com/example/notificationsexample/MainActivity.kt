package com.example.notificationsexample

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.RemoteInput
import com.example.notificationsexample.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManagerCompat

    companion object {
        val messages: MutableList<Message> = mutableListOf()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        notificationManager = NotificationManagerCompat.from(this)

        sendOnChannel1()
        sendOnChannel2()

        messages.add(Message("Good Morning!", "Jim"))
        messages.add(Message("Hello!", null))
        messages.add(Message("Hi!", "Jenny"))
    }

    private fun sendOnChannel1() {
        binding.btnSendOnChannel1.setOnClickListener {
            SendOnChannel1Notification.sendOnChannel1Notification(this)
        }
    }

    object SendOnChannel1Notification {
        fun sendOnChannel1Notification(context: Context?) {

            val activityIntent = Intent(context, MainActivity::class.java)
            val contentIntent = PendingIntent.getActivity(context, 0, activityIntent, 0)

            val remoteInput = RemoteInput.Builder("key_text_reply")
                .setLabel("You answer...")
                .build()

            var replyIntent = Intent(context, DirectReplyReceiver::class.java)
            var replyPendingIntent = PendingIntent.getBroadcast(
                context, 0, replyIntent, 0
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                replyIntent = Intent(context, DirectReplyReceiver::class.java)
                replyPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0, replyIntent, 0
                )
            } else {
                //start chat activity instead (PendingIntent.getActivity)
                //cancel notification with notificationManagerCompat.cancel(id)
            }

            val replyAction =
                NotificationCompat.Action.Builder(R.drawable.ic_send, "Reply", replyPendingIntent)
                    .addRemoteInput(remoteInput)
                    .build()

            val messagingStyle = NotificationCompat.MessagingStyle("Me")
            messagingStyle.conversationTitle = "Group Chat"

            for (chatMessage in messages) {
                val notificationMessage = NotificationCompat.MessagingStyle.Message(
                    chatMessage.text, chatMessage.timestamp,
                    chatMessage.sender
                )
                messagingStyle.addMessage(notificationMessage)
            }

            val notification = NotificationCompat.Builder(context!!, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_one)
                .setStyle(messagingStyle)
                .addAction(replyAction)
                .setColor(Color.BLUE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build()

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(1, notification)
        }
    }

    private fun sendOnChannel2() {
        binding.btnSendOnChannel2.setOnClickListener {

            val title1 = "Title1"
            val message1 = "Message1"
            val title2 = "Title2"
            val message2 = "Message2"

            val notification1 = NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_two)
                .setContentTitle(title1)
                .setContentText(message1)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("example_group")
                .build()

            val notification2 = NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_two)
                .setContentTitle(title2)
                .setContentText(message2)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("example_group")
                .build()

            val summaryNotification = NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_send)
                .setStyle(NotificationCompat.InboxStyle()
                    .addLine("$title2 $message2")
                    .addLine("$title1 $message1")
                    .setBigContentTitle("2 new messages")
                    .setSummaryText("user@example.com")
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setGroup("example_group")
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
                .setGroupSummary(true)
                .build()

            SystemClock.sleep(2000)
            notificationManager.notify(2, notification1)
            SystemClock.sleep(2000)
            notificationManager.notify(3, notification2)
            SystemClock.sleep(2000)
            notificationManager.notify(4, summaryNotification)
        }
    }
}