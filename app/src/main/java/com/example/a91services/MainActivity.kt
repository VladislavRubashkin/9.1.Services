package com.example.a91services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.app.job.JobWorkItem
import android.content.ComponentName
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.a91services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.simpleService.setOnClickListener {
            stopService(MyForegroundService.newIntent(this)) // TODO#5.1 Остановка сервиса из вне
            startService(MyService.newIntent(this, 10))
        }
        binding.foregroundService.setOnClickListener {
            ContextCompat.startForegroundService(this, MyForegroundService.newIntent(this))
        }
        binding.intentService.setOnClickListener {
            ContextCompat.startForegroundService(this, MyIntentService.newIntent(this))
        }
        /**
         * TODO#7.3
         *
         * Запуск JobService
         * componentName - указываем необходимый сервис.
         * jobInfo - содержит все требования для нашего сервиса:
         * setRequiresCharging(true) - сервис будет работать только если телефон подключен к зарядке
         * setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED) - сервис будет работать только если телефон
         * подключен к wi-fi.
         * jobScheduler - планируем выполнение сервиса
         *
         * setPersisted(true) - если телефон выключили и потом включили, перезапустит сервис, требует permission
         * <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
         *
         * TODO#8.2
         *
         * val intent = MyJobService.newIntent(page++) - создание и передача параметров с помощью метода enqueue() -
         * ставим их в очередь на выполнение
         * в метод enqueue() - передаётся jobInfo и объект типа JobWorkItem(), в который кладётся интент с параметрами
         *
         * jobScheduler.schedule() - если запустить сервис с помощью метода schedule() - работать будет только последний,
         * все предыдущие сервисы будут отменены, если мы хотим выполнить их все, то их необходимо ставить в очередь на
         * выполнение - метод enqueue()
         */
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)
            val jobInfo = JobInfo.Builder(MyJobService.JOB_SERVICE_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .build()

            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val intent = MyJobService.newIntent(page++)
                jobScheduler.enqueue(jobInfo, JobWorkItem(intent))
            }

        }
    }

}