package com.example.myworkmanagerdemo

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloadWorker(context: Context, parameters : WorkerParameters): Worker(context, parameters) {

    @SuppressLint("RestrictedApi")
    override fun doWork(): Result {

        try{
            for(i in 0..3000){
                Log.i("MYTAG","Downloading $i")

            }

            return Result.Success()
        }
        catch (e: Exception){
            return Result.failure()
        }

    }
}