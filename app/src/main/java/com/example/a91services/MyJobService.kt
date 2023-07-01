package com.example.a91services

import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.PersistableBundle
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
     * Если делаем асинхронную работу и окончание метода onStartJob() НЕ означает окончание работы - то мы сами должны
     * завершать работу - метод jobFinished().
     * метод jobFinished() - принимает два параметра. Первый параметры из метода onStartJob(), второй - нужно ли
     * запланировать выполнение сервиса заново(напр мы хотим сделать обновление данных в фоне).
     */
    /**
     * TODO#8
     *
     * Допустим мы хотим загружать данные из сети для десяти страниц нашего приложения, для каждой страницы по пять
     * единиц данных. Если загрузка уже идёт и при этом перестало выполняться какое-то условие для работы нашего
     * сервиса(напр телефон отключили от зарядки), а потом сразу подключили - сервис перезапустится...с начала и начнет
     * подгружать уже загруженные данные.
     *
     * TODO#8.3
     *
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) - работать с очередью сервисов можно только с API26
     * var workItem = params?.dequeueWork() - извлекаем элемент(первый сервис) из очереди сервисов
     * while (workItem != null) - работать будем пока в очереди есть элементы для выполнения
     * val page = workItem.intent.getIntExtra(PAGE, 0) - получаем из элемента очереди интент, из которого получаем значение
     * крутимся во внутреннем цикле(выполняем работу)
     * params?.completeWork(workItem) - данный сервис из очереди закончил свою работу
     * workItem = params?.dequeueWork() - достаём следующий объект из очереди
     *  jobFinished(params, false) - завершаем работу всего сервиса(уже после того как в очереди не осталось элементов
     *  для выполнения)
     */
    override fun onStartJob(params: JobParameters?): Boolean {
        log("onStartJob")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            coroutineScope.launch {
                var workItem = params?.dequeueWork()
                while (workItem != null) {
                    val page = workItem.intent.getIntExtra(PAGE, 0)
                    for (i in 0 until 5) {
                        delay(1000)
                        log("Timer - value: $i , page: $page")
                    }
                    params?.completeWork(workItem)
                    workItem = params?.dequeueWork()
                }
                jobFinished(params, false)
            }
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

    /**
     * TODO#8.1
     *
     * Создаём PersistableBundle, в него кладём параметры(похоже на интент)
     */
    companion object {

        const val JOB_SERVICE_ID = 1
        private const val PAGE = "page"

        fun newIntent(page: Int): Intent {
            return Intent().apply {
                putExtra(PAGE, page)
            }
        }
    }
}