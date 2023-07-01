package com.example.a91services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat


class MyIntentServiceAndJobScheduler : IntentService(SERVICE_NAME) {

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
        setIntentRedelivery(true)
    }

    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        val page = intent?.getIntExtra(PAGE_NUMBER, 0) ?: 0
        for (i in 0 until  10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyForegroundService + $message")
    }

    companion object {

        private const val SERVICE_NAME = "my_intent_service"
        private const val PAGE_NUMBER = "page_number"

        fun newIntent(context: Context, page: Int): Intent {
            return Intent(context, MyIntentServiceAndJobScheduler::class.java).apply {
                putExtra(PAGE_NUMBER, page)
            }
        }
    }
}