package com.actaks.nexledger.feature.notifications.di

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.actaks.nexledger.feature.notifications.DailyCheckWorker
import com.actaks.nexledger.feature.notifications.NotificationChannels
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val notificationsDiModule = module {
    single { NotificationChannels() }
    single { WorkManager.getInstance(get<Context>()) }
}

/**
 * Must be called after Koin is started to schedule the daily background check.
 */
fun scheduleDailyCheck(context: Context) {
    val dailyRequest =
        PeriodicWorkRequestBuilder<DailyCheckWorker>(1, TimeUnit.DAYS).setInitialDelay(
                1,
                TimeUnit.HOURS
            ).build()
    WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "nexledger_daily_check", ExistingPeriodicWorkPolicy.KEEP, dailyRequest
        )
}
