package com.example.a91services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

/**
 * TODO#1
 *
 * ЖЦ сервисов
 * onCreate()(создаётся) -> onStartCommand()(выполняется вся работа) -> onDestroy()(уничтожается)
 * Service() - предназначены для выполнения каких-то задач в фоне
 * Для создания сервисов необходимо унаследоваться от класса Service()
 * По умолчанию код внутри сервиса выполняется на главном потоке
 * Четыре основных компонента android приложения: Activity, Service, Broadcast Receivers,
 *  Content Providers - регистрируются в Manifest
 * Чтобы запустить сервис, нужно вызвать метод startService() в активити и передать Intent в качестве параметра
 */
class MyService : Service() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    /**
     * TODO#2
     *
     * !!!!!Не смотря на то что сервисы предназначены для работы в фоне, КОД ВНУТРИ МЕТОДА onStartCommand() -
     * ВЫПОЛНЯЕТСЯ НА ГЛАВНОМ ПОТОКЕ!!!!!
     * Поэтому запускаем его в корутине.
     *
     * Возвращаемое значение:
     * START_STICKY - если система убьет сервис, то он будет пересоздан(аналогично вызову конструктора
     * супер-класса - super.onStartCommand(intent, flags, startId)).
     * START_NOT_STICKY - если система убьет сервис, то он НЕ будет пересоздан.
     * START_REDELIVER_INTENT - если мы принимаем какое-то значение из интента и хотим чтобы в случае пересоздания сервиса
     * это значение снова прилетало в интент(а так работа аналогична START_STICKY)
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        val start = intent?.getIntExtra(EXTRA_START, 0) ?: 0
        coroutineScope.launch {
            for (i in start until start + 100) {
                delay(1000)
                log("Timer $i")
            }
        }
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyService + $message")
    }

    companion object {

        private const val EXTRA_START = "start"

        fun newIntent(context: Context, start: Int): Intent {
            return Intent(context, MyService::class.java).apply {
                putExtra(EXTRA_START, start)
            }
        }
    }
}