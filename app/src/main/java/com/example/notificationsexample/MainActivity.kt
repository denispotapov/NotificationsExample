package com.example.notificationsexample

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
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


            val picture = BitmapFactory.decodeResource(resources, R.drawable.cat)

            val notification = NotificationCompat.Builder(this, CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_one)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(picture)
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(picture)
                        .bigLargeIcon(null)
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build()

            notificationManager.notify(1, notification)
        }
    }

    private fun sendOnChannel2() {

        binding.btnSendOnChannel2.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val message = binding.editTextMessage.text.toString()

            val artWork = BitmapFactory.decodeResource(resources, R.drawable.cat2)

            val mediaSession = MediaSessionCompat(this, "tag")

            val notification = NotificationCompat.Builder(this, CHANNEL_2_ID)
                .setSmallIcon(R.drawable.ic_baseline_looks_two)
                .setContentTitle(title)
                .setContentText(message)
                .setLargeIcon(artWork)
                .addAction(R.drawable.ic_dislike, "Dislike", null)
                .addAction(R.drawable.ic_previous, "Previous", null)
                .addAction(R.drawable.ic_pause, "Pause", null)
                .addAction(R.drawable.ic_next, "Next", null)
                .addAction(R.drawable.ic_like, "Like", null)
                .setStyle(androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(1, 2, 3)
                    .setMediaSession(mediaSession.sessionToken)
                )
                .setSubText("Sub Text")
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

            notificationManager.notify(2, notification)
        }
    }
}