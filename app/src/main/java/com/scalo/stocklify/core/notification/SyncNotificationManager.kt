package com.scalo.stocklify.core.notification

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.scalo.stocklify.R
import com.scalo.stocklify.core.util.snakeToTitleCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncNotificationManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Sound/importance can only be set at channel creation — Android locks them
            // once the channel exists, so this only takes effect for installs that haven't
            // created CHANNEL_ID before (hence the _v2 suffix bumping past the old channel).
            notificationManager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "Sync", IMPORTANCE_DEFAULT).apply {
                    description = "Shows the status of data synchronization"
                    setSound(
                        RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .build()
                    )
                }
            )
        }
    }

    fun showSyncing() {
        notify(
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setLargeIcon(logoBitmap())
                .setContentTitle("Syncing")
                .setContentText("Syncing your data…")
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        )
    }

    fun showSuccess() {
        notify(
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setLargeIcon(logoBitmap())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle("Sync complete")
                .setContentText("Your data is up to date.")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
        )
    }

    fun showError(tableName: String?) {
        val text = if (tableName != null) {
            "Something went wrong while syncing ${tableName.snakeToTitleCase()}."
        } else {
            "Something went wrong while syncing."
        }
        notify(
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle("Sync failed")
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build()
        )
    }

    /** setSmallIcon must stay a flat silhouette (status bar strips color) — the full-color
     *  logo is only shown via setLargeIcon, which supports real bitmaps. */
    private fun logoBitmap() = BitmapFactory.decodeResource(context.resources, R.drawable.logo)

    @SuppressLint("MissingPermission")
    private fun notify(notification: android.app.Notification) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        // Bumped from "sync_channel" — channel importance/sound are locked in at creation,
        // so existing installs need a new id to pick up IMPORTANCE_DEFAULT + sound.
        private const val CHANNEL_ID = "sync_channel_v2"
        private const val NOTIFICATION_ID = 1001
    }
}
