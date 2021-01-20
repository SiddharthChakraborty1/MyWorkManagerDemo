package com.example.myworkmanagerdemo

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.text.SimpleDateFormat
import java.util.*

class UploadWorker(context: Context, parameters: WorkerParameters): Worker(context, parameters) {
    companion object{
        const val KEY_WORKER = "key_worker"
    }
    override fun doWork(): Result {
        val count = inputData.getInt(MainActivity.KEY_COUNT,0)
        try {
            for(i in 0 until count){
                Log.i("MYTAG"," Uploaded user $i")

            }
            val time = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            val date = time.format(Date())

            val data = Data.Builder()
                .putString(KEY_WORKER,date)
                .build()
            return Result.success(data)

        }
        catch (e: Exception){
            return Result.failure()
        }
    }
}