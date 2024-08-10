package com.hifx.workmanagerexample.work

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class SomeWork(
    val context: Context,
    val workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        println("SomeWork>>>doWork start")
        val paramData = inputData.getString("key-one") ?: ""
        runBlocking {
            println("SomeWork>>>doWork start work")
            loadNotes {
                println("SomeWork>>>doWork loadNotes callback ${it.size}")
                it.forEach { item ->
                    println("SomeWork>>>doWork item -> ${item.title}\n")
                }
            }
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

    fun loadNotes(
        callBack: (imageList: List<NoteItemModel>) -> Unit,
    ) {
        println("SomeWork>>>doWork loadNotes")
        CoroutineScope(Dispatchers.IO).launch {
            val AUTHORITY = "com.promode.note.provider"
            val noteAllUri: Uri = Uri.parse("content://$AUTHORITY/notes")
            println("uri : $noteAllUri")
            val result = context.contentResolver.query(
                noteAllUri,
                null,
                null,
                null,
                null,
            )
            val list = arrayListOf<NoteItemModel>()
            result?.let { cursor ->
                val idColumn = cursor.getColumnIndex("id")
                val titleColumn = cursor.getColumnIndex("title")
                val dateColumn = cursor.getColumnIndex("date")
                while (cursor.moveToNext()) {
                    val id = cursor.getInt(idColumn)
                    val title = cursor.getString(titleColumn)
                    val date = cursor.getLong(dateColumn)
                    list.add(
                        NoteItemModel(
                            id,
                            title,
                            date.toString(),
                        ),
                    )
                }
            }
            result?.close()
            withContext(Dispatchers.Main) {
                callBack.invoke(list)
            }
        }
    }
}

data class NoteItemModel(
    val id: Int,
    val title: String,
    val date: String,
)
