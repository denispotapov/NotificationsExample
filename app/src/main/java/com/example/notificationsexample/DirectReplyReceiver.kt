package com.example.notificationsexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.RemoteInput

class DirectReplyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val remoteInput = RemoteInput.getResultsFromIntent(intent)

        if (remoteInput != null) {
            val replyText = remoteInput.getCharSequence("key_text_reply")
            val answer = Message(replyText, null)
            MainActivity.messages.add(answer)

            MainActivity.SendOnChannel1Notification.sendOnChannel1Notification(context)
        }
    }
}