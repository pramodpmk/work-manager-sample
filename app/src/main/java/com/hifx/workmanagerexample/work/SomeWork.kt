package com.hifx.workmanagerexample.work

import android.content.Context
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SomeWork(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        println("SomeWork>>>doWork start")
        val paramData = inputData.getString("key-one") ?: ""
        runBlocking {
            println("SomeWork>>>doWork start work")
            delay(15000)
            println("SomeWork>>>doWork end work")
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    applicationContext,
                    "Work complete - $paramData",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
        println("SomeWork>>>doWork Result return")
        return Result.success()
    }
}
