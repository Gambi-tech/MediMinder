package com.example.mediminder

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationReceiver : BroadcastReceiver() {

    @SuppressLint("NotificationPermission", "MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = 101

        val details = intent?.getStringExtra("details")

        // Extracting details
        val lines = details?.lines()

        val medicineName = lines?.get(0)?.substringAfter("medicine_name: ")?.trim()
        val dosage = lines?.get(1)?.substringAfter("dosage: ")?.trim()
        val intakeTimeString = lines?.get(2)?.substringAfter("intake_time: ")?.trim()

        // Convert intake_time to Long
        val intakeTimeMillis = intakeTimeString?.toLongOrNull() ?: 0L
        // Convert the intake time from milliseconds to a human-readable time format
        val intakeTime = Date(intakeTimeMillis)
        val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        val formattedTime = timeFormat.format(intakeTime)

        //Add the pills icon here
//        val largeIcon = BitmapFactory.decodeResource(context!!.resources, R.drawable.pills)

        val notificationBuilder = context?.let {
            NotificationCompat.Builder(it, "medication_reminder_channel")
                //Add a notification icon
//                .setSmallIcon(R.drawable.ic_notification)
//                .setLargeIcon(largeIcon)
                .setContentTitle("Medication Reminder")
                .setContentText("It's $formattedTime now! Time to take your medication. Name: $medicineName, Dosage: $dosage ")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
        }

        val notificationManager = context?.let { NotificationManagerCompat.from(it) }
        notificationBuilder?.let {
            notificationManager?.notify(notificationId, it.build())
        }
    }
}