package com.example.a91services

import android.content.Context
import android.util.Log
import androidx.work.*


/**
 * TODO#11
 *
 * implementation 'androidx.work:work-runtime-ktx:2.8.1'
 * Наследуемся от класса Worker()
 * Переопределить метод doWork() - вся работа происходит в нём
 * Метод doWork() - выполняется в фоновом потоке
 * Нам прилетает НЕ intent а workerParameters, из них и можно достать данные
 *
 * Result.success() - успешно
 * Result.failure() - ошибка, НЕ будет перезапущен
 * Result.retry() - ошибка, будет перезапущен
 *
 *
 */
class MyWorker(
    context: Context,
    private val workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    override fun doWork(): Result {
        log("doWork")
        val page = workerParameters.inputData.getInt(PAGE_NUMBER, 0)
        for (i in 0 until 10) {
            Thread.sleep(1000)
            log("Timer $i $page")
        }
        return Result.success()
    }

    private fun log(message: String) {
        Log.d("Service_log", "MyWorker + $message")
    }

    /**
     * TODO#11.2
     *
     * OneTimeWorkRequestBuilder<MyWorker>() - с помощью билдера создаём объект OneTimeWorkRequest
     * setInputData() - кладём в него необходимые данные(workDataOf() - принимает пары ключ-значение)
     * setConstraints() - устанавливаем ограничения
     */
    companion object {

        private const val PAGE_NUMBER = "page_number"
        const val WORK_NAME = "worker_name"

        fun makeRequest(page: Int): OneTimeWorkRequest {
            return OneTimeWorkRequestBuilder<MyWorker>().apply {
                setInputData(workDataOf(PAGE_NUMBER to page))
                setConstraints(makeConstraints())
            }.build()
        }

        private fun makeConstraints(): Constraints {
            return Constraints.Builder()
                .setRequiresCharging(true)
                .build()
        }
    }
}