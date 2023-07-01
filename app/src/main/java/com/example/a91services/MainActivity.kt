package com.example.a91services

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.a91services.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

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
         */
        binding.jobScheduler.setOnClickListener {
            val componentName = ComponentName(this, MyJobService::class.java)
            val jobInfo = JobInfo.Builder(MyJobService.JOB_SERVICE_ID, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .build()
            val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            jobScheduler.schedule(jobInfo)

        }
    }

}