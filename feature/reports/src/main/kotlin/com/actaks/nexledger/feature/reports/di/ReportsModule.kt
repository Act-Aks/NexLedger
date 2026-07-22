package com.actaks.nexledger.feature.reports.di

import com.actaks.nexledger.feature.reports.ReportViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val reportsModule = module {
    viewModel { ReportViewModel(get()) }
}