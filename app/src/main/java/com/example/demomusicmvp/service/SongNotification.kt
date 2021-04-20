package com.example.demomusicmvp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.demomusicmvp.R
import com.example.demomusicmvp.utils.ConstantExt.NOTIFY_ID

class SongNotification {
    fun sendNotification(context: Context, title: String) {
        val notificationChannels =
            NotificationChannel(
                NOTIFY_ID.toString(),
                context.getString(R.string.title_song_playing_state),
                NotificationManager.IMPORTANCE_DEFAULT
            )
        val serviceNotify =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        serviceNotify.createNotificationChannel(notificationChannels)
        val notificationBuilder = NotificationCompat.Builder(
            context, NOTIFY_ID.toString()
        )
        notification = notificationBuilder
            .setSmallIcon(R.drawable.ic_music_note)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(Notification.CATEGORY_SERVICE)
            .setContentTitle(context.getString(R.string.title_song_playing_state))
            .setContentText(title)
            .build()
    }

    companion object {
        var notification: Notification? = null
    }
}
