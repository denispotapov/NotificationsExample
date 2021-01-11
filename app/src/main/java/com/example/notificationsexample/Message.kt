package com.example.notificationsexample

class Message(val text: CharSequence?, val sender: CharSequence?) {
    val timestamp: Long = System.currentTimeMillis()
}