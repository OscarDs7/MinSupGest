package com.example.minsupgest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationUtils {
    private const val CHANNEL_ID = "stock_channel"
    private var notificationId = 1

    fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
        val name = "Notificaciones de Stock"
        val descriptionText = "Avisos de productos con bajo stock"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification) // Usa un ícono válido
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        try {
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId++, builder.build())
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }
}
