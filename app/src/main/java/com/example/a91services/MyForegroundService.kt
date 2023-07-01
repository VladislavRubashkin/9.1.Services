package com.example.a91services

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
 * TODO#4
 *
 * Для запуска ForegroundService потребуется:
 * в методе override fun onCreate() вызвать метод startForeground()
 * в манифесте <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 * создать NotificationChannel для версий андроид API 26(OREO)
 * запускать такой сервис необходимо методом  ContextCompat.startForegroundService()
 */
class MyForegroundService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        coroutineScope.launch {
            for (i in 0 until  100) {
                delay(1000)
                log("Timer $i")
            }
            /**
             * TODO#5
             *
             * Остановка сервиса. Вызывается изнутри сервиса.
             */
            stopSelf()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyForegroundService + $message")
    }

    /**
     * TODO#3
     *
     * Начиная с восьмой версии android(API 26(OREO)) ввели ограничения на использования сервисов.
     * Если мы хотим выполнять какую-то работу в фоне - об этом необходимо обязательно уведомлять пользователя.
     * Пока работает сервис - должно висеть уведомление.
     *
     * Создать само уведомление(обязательно установить - заголовок, текст, иконку)
     * Создать notificationManager
     * Добавить permission "android.permission.POST_NOTIFICATIONS"
     */
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Title")
            .setContentText("Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    /**
     * TODO#3.1
     *
     * Для каждого уведомления(notification) необходимо создать какой-то канал(notification channel) - с
     * версии android(API 26(OREO))
     */
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

        fun newIntent(context: Context): Intent {
            return Intent(context, MyForegroundService::class.java)
        }
    }
}