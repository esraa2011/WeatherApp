package com.example.weatherapp.ui.alert.BroadCastReciver

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.widget.RemoteViews
import com.example.weatherapp.R

class AlertBroadCastReceiver : BroadcastReceiver() {

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel

    override fun onReceive(context: Context, intent: Intent) {

        notification(context)
    }
}

@SuppressLint("RemoteViewLayout")
private fun notification(context: Context) {

    lateinit var builder: Notification.Builder

    val channelId = "i.apps.notifications"

    val description = "Test notification"

    var notificationManager: NotificationManager =
        context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val contentView = RemoteViews(context.packageName, R.layout.alert_fragment)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var notificationChannel =
            NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)

        notificationChannel.enableLights(true)

        notificationChannel.lightColor = Color.GREEN

        notificationChannel.enableVibration(false)

        notificationManager.createNotificationChannel(notificationChannel)

        builder = Notification.Builder(context, channelId)
            .setContent(contentView)

            .setSmallIcon(R.drawable.pressure_icon)

            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.cloud_icon))

        // .setContentIntent(pendingIntent)
    } else {

        builder = Notification.Builder(context)
            .setContent(contentView)

            .setSmallIcon(R.drawable.uv_icon)

            .setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_launcher_background
                )
            )
        //.setContentIntent(pendingIntent)
    }
    notificationManager.notify(1234, builder.build())
}
