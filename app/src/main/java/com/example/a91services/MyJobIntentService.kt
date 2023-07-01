package com.example.a91services

import android.app.job.JobWorkItem
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

/**
 * TODO#10
 *
 * MyJobIntentService() - комбинирование JobService и IntentService.
 * Переопределить метод onHandleWork().
 * запуск JobIntentService() - статический метод enqueue().
 * в Manifest требуется permission - android:permission="android.permission.BIND_JOB_SERVICE".
 * в Manifest требуется permission - <uses-permission android:name="android.permission.WAKE_LOCK" />
 */
class MyJobIntentService : JobIntentService() {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onHandleWork(intent: Intent) {
        log("onHandleWork")
        val page = intent.getIntExtra(PAGE_NUMBER, 0)
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyJobIntentService + $message")
    }

    companion object {

        private const val PAGE_NUMBER = "page_number"
        private const val JOB_ID = 111

        fun enqueue(context: Context, page: Int) {
            enqueueWork(
                context,
                MyJobIntentService::class.java,
                JOB_ID,
                newIntent(context, page)
            )
        }

        private fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyJobIntentService::class.java).apply {
                putExtra(PAGE_NUMBER, page)
            }
        }
    }
}