package com.actaks.nexledger.feature.statistics.di

import com.actaks.nexledger.feature.statistics.StatisticViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val statisticsModule = module {
    viewModel { StatisticViewModel(get(), get()) }
}
