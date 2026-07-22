package com.actaks.nexledger.core.datastore.di

import com.actaks.nexledger.core.datastore.NexLedgerPreferences
import org.koin.dsl.module

val datastoreModule = module {
    single { NexLedgerPreferences(get()) }
}