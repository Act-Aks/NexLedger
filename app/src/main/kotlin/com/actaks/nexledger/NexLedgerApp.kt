package com.actaks.nexledger

import android.app.Application
import com.actaks.nexledger.core.database.di.databaseModule
import com.actaks.nexledger.core.database.di.repositoryModule
import com.actaks.nexledger.core.datastore.di.datastoreModule
import com.actaks.nexledger.feature.accounts.di.accountsModule
import com.actaks.nexledger.feature.backup.presentation.di.backupModule
import com.actaks.nexledger.feature.budgets.presentation.di.budgetsModule
import com.actaks.nexledger.feature.categories.presentation.di.categoryModule
import com.actaks.nexledger.feature.dashboard.presentation.di.dashboardModule
import com.actaks.nexledger.feature.goals.presentation.di.goalsModule
import com.actaks.nexledger.feature.notifications.di.notificationsDiModule
import com.actaks.nexledger.feature.notifications.di.scheduleDailyCheck
import com.actaks.nexledger.feature.reports.di.reportsModule
import com.actaks.nexledger.feature.search.di.searchModule
import com.actaks.nexledger.feature.security.di.securityModule
import com.actaks.nexledger.feature.settings.di.settingsModule
import com.actaks.nexledger.feature.statistics.di.statisticsModule
import com.actaks.nexledger.feature.transactions.di.transactionsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NexLedgerApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@NexLedgerApp)
            modules(
                databaseModule,
                repositoryModule,
                datastoreModule,
                notificationsDiModule,
                dashboardModule,
                accountsModule,
                categoryModule,
                transactionsModule,
                budgetsModule,
                goalsModule,
                reportsModule,
                statisticsModule,
                searchModule,
                backupModule,
                settingsModule,
                securityModule
            )
        }
        scheduleDailyCheck(this)
    }
}