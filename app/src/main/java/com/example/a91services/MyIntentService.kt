package com.example.a91services

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

/**
 * TODO#6
 *
 * IntentService
 * Уже Deprecated, но знать полезно
 * Наследуемся от IntentService()
 * В конструктор указываем имя сервиса
 * Переопределяем метод  override fun onHandleIntent(intent: Intent?)
 */
class MyIntentService : IntentService(SERVICE_NAME) {

    /**
     * TODO#6.2
     *
     * setIntentRedelivery(true) - аналог START_REDELIVER_INTENT
     * setIntentRedelivery(false) - аналог START_NOT_STICKY
     * отсутствие вызова этого метода - аналог START_STICKY
     */
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        log("onCreate")
    }

    /**
     * TODO#6.1
     *
     * Главные отличия от обычного сервиса
     * Переопределяем метод onHandleIntent() а не onStartCommand()
     * Код внутри метода onHandleIntent() будет выполняться на второстепенном потоке
     * После выполнения работы в этом методе, сервис будет остановлен автоматически
     * Одновременно будет работать только один сервис
     */
    override fun onHandleIntent(intent: Intent?) {
        log("onHandleIntent")
        for (i in 0 until  10) {
            Thread.sleep(1000)
            log("Timer $i")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyIntentService + $message")
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {

        private const val CHANNEL_ID = "channel_1"
        private const val CHANNEL_NAME = "channel_name"
        private const val NOTIFICATION_ID = 1
        private const val SERVICE_NAME = "my_intent_service"

        fun newIntent(context: Context): Intent {
            return Intent(context, MyIntentService::class.java)
        }
    }
}