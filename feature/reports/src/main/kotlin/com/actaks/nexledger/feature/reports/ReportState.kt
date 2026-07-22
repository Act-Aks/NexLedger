package com.actaks.nexledger.feature.reports

data class ReportState(
    val period: ReportPeriod = ReportPeriod.MONTHLY,
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val netSavings: Double = 0.0,
    val loading: Boolean = false,
    val error: String? = null
)

enum class ReportPeriod { MONTHLY, YEARLY }