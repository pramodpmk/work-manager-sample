package com.hifx.workmanagerexample.work

import android.content.Context
import android.widget.Toast
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SomeRecurringWork(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        println("SomeWork>>>doWork start")
        println("SomeWork>>>doWork start work")
        delay(15000)
        println("SomeWork>>>doWork end work")
        withContext(Dispatchers.Main) {
            Toast.makeText(applicationContext, "Work complete", Toast.LENGTH_LONG).show()
        }
        println("SomeWork>>>doWork Result return")
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }
}
