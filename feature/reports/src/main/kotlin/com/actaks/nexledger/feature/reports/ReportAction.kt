package com.actaks.nexledger.feature.reports

sealed interface ReportAction {
    data class OnPeriodSelect(val period: ReportPeriod) : ReportAction
    data object OnGenerate : ReportAction
    data object OnErrorDismissed : ReportAction
}