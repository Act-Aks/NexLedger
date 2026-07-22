package com.actaks.nexledger.feature.backup.presentation.di

import com.actaks.nexledger.feature.backup.data.BackupRepository
import com.actaks.nexledger.feature.backup.data.BackupRepositoryImpl
import com.actaks.nexledger.feature.backup.presentation.BackupViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val backupModule = module {
    single<BackupRepository> { BackupRepositoryImpl(get(), get(), get(), androidContext()) }
    viewModel { BackupViewModel(get()) }
}