package com.example.a91services

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import kotlinx.coroutines.*

/**
 * TODO#7
 *
 * Унаследоваться от JobService()
 * Переопределить два метода onStartJob() и onStopJob()
 * В Manifest - внутри тега <service> -  android:permission="android.permission.BIND_JOB_SERVICE"
 * На эти сервисы можно устанавливать ограничение(напр сервис выполняется только если телефон заряжается или
 * данные скачиваются только когда телефон подключен к wi-fi и пр)
 */
class MyJobService : JobService() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    /**
     * TODO#7.1
     *
     * Выполняется на главном потоке
     * Возвращаемое значение - работа ещё выполняется или уже нет, в данном случае мы запускаем корутину на выполнение и
     * затем выходим из метода - соответственно работа ещё выполняется - возвращаем true.
     * А если бы мы делали какую-то синхронную работу и окончание работы метода onStartJob() означало бы что сервис
     * закончил выполнение работы - возвращаем false.
     * Если делаем асинхронную работу и окончание метода onStartJob() НЕ означает окончание работы - то мы сам должны
     * завершать работу - метод jobFinished().
     * метод jobFinished() - принимает два параметра. Первый параметры из метода onStartJob(), второй - нужно ли
     * запланировать выполнение сервиса заново(напр мы хотим сделать обновление данных в фоне).
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")
        coroutineScope.launch {
            for (i in 0 until 100) {
                delay(1000)
                log("Timer $i")
            }
            jobFinished(params, false)
        }
        return true
    }

    /**
     * TODO#7.2
     *
     * Если мы сами останавливаем сервис методом jobFinished() - метод onStopJob() НЕ вызывается.
     * Запланировать выполнение сервиса заново - return true в противном случае return false.
     */
    override fun onStopJob(params: JobParameters?): Boolean {
        log("onStopJob")
        return true
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        log("onDestroy")
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyJobService + $message")
    }

    companion object {

        const val JOB_SERVICE_ID = 1
    }
}