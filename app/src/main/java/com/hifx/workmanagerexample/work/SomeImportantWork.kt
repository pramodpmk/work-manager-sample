package com.hifx.workmanagerexample.work

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hifx.workmanagerexample.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SomeImportantWork(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        println("SomeWork>>>doImportantWork start")
        runBlocking {
            println("SomeWork>>>doImportantWork start work")
            delay(15000)
            println("SomeWork>>>doImportantWork end work")
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Work complete", Toast.LENGTH_LONG).show()
            }
        }
        println("SomeWork>>>doImportantWork Result return")
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val channelId = "LONG_RUNNING_TASK_CHANNEL"
        val title = "Long Running Task"
        val cancel = "Cancel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_LOW)
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, WorkManager.getInstance(applicationContext).createCancelPendingIntent(id))
            .build()
        return ForegroundInfo(
            1000,
            notification,
        )
    }
}
