package com.example.notificationsexample

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.notificationsexample.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var notificationManager: NotificationManagerCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        notificationManager = NotificationManagerCompat.from(this)

        sendOnChannel1()
        sendOnChannel2()
    }

    private fun sendOnChannel1() {

        binding.btnSendOnChannel1.setOnClickListener {

            val title = binding.editTextTitle.text.toString()
            val message = binding.editTextMessage.text.toString()

            val activityIntent = Intent(this, MainActivity::class.java)
            val contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0)

            val broadcastIntent = Intent(this, NotificationReceiver::class.java)
            broadcastIntent.putExtra("toastMessage", message)
            val actionIntent =
                PendingIntent.getBroadcast(
                    this,
                    0,
                    broadcastIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

            val largeIcon = BitmapFactory.decodeResource(resources, R.drawable.cat)

            val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_one)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(largeIcon)
                .setStyle(
                    NotificationCompat.BigTextStyle()
                        .setBigContentTitle("Big Content Title")
                        .setSummaryText("Summary Text")
                        .bigText(getString(R.string.long_text))
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.BLUE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_baseline_arrow_forward, "Toast", actionIntent)
                .build()

            notificationManager.notify(1, notification)
        }
    }

    private fun sendOnChannel2() {

        binding.btnSendOnChannel2.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val message = binding.editTextMessage.text.toString()

            val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_two)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(
                    NotificationCompat.InboxStyle()
                        .setBigContentTitle("Big Content Title")
                        .setSummaryText("Summary Text")
                        .addLine("This is line 1")
                        .addLine("This is line 2")
                        .addLine("This is line 3")
                        .addLine("This is line 4")
                        .addLine("This is line 5")
                        .addLine("This is line 6")
                        .addLine("This is line 7")
                )
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

            notificationManager.notify(2, notification)
        }
    }
}