package com.app.fitlife

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

class Worker (context: Context, workerParameter: WorkerParameters): Worker(context, workerParameter) {

    companion object {
        const val CHANNEL_ID = "channel_id"
        const val NOTIFICATION_ID = 1
    }

    override fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    @SuppressLint("UnspecifiedImmutableFlag", "MissingPermission")
    private fun showNotification() {
        val intent = Intent(applicationContext, SignUpActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent, 0
        )

        val notification = NotificationCompat.Builder(
            applicationContext, CHANNEL_ID
        )

            .setSmallIcon(R.drawable.drink_water_foreground)
            .setContentTitle("Já Bebeu a sua água hoje?")
            .setContentText("É hora de beber agua!")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = "Channel Name"
            val channelDescription = "ChannelDescription"
            val channelImportance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
                description = channelDescription
            }
            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notification.build())
        }
    }
}
