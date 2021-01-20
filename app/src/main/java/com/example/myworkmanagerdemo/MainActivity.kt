package com.example.myworkmanagerdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.work.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            startOneTimeTask()
            startPeriodicTask()
        }
    }
    companion object{
        const val KEY_COUNT = "key_count"
    }

    private fun startOneTimeTask(){
        val workManager = WorkManager.getInstance(applicationContext)
        val data = Data.Builder()
            .putInt(KEY_COUNT, 125)
            .build()
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()
        val uploadTask = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        val filterRequest = OneTimeWorkRequest.Builder(FilterWorker::class.java)
            .build()

        val compressRequest = OneTimeWorkRequest.Builder(CompressWorker::class.java)
            .build()

        val downloadRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
            .build()

        val parallelWorks = mutableListOf<OneTimeWorkRequest>()
        parallelWorks.add(filterRequest)
        parallelWorks.add(downloadRequest)

        workManager.beginWith(parallelWorks)
            .then(compressRequest)
            .then(uploadTask)
            .enqueue()



        workManager.getWorkInfoByIdLiveData(uploadTask.id).observe(this, Observer {
            Log.i("MYTAG", "The state is ${it.state.name}")
            textView.text = it.state.name

            if(it.state.isFinished){
                val data = it.outputData
                val date = data.getString(UploadWorker.KEY_WORKER)
                Log.i("MYTAG","The date is $date")
                Toast.makeText(applicationContext, date, Toast.LENGTH_SHORT).show()
            }
        })



    }

    private fun startPeriodicTask(){
        val periodicWorkRequest = PeriodicWorkRequest.Builder(DownloadWorker::class.java,16, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueue(periodicWorkRequest)
    }
}