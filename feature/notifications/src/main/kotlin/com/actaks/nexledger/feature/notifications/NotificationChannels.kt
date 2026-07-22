package com.actaks.nexledger.feature.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context


class NotificationChannels {

    fun createChannels(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)

        listOf(
            NotificationChannel(BUDGET_ALERTS, "Budget Alerts", NotificationManager.IMPORTANCE_HIGH)
                .apply { description = "Alerts when you're approaching or exceeding your budget" },
            NotificationChannel(
                GOAL_REMINDERS,
                "Goal Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply { description = "Reminders to stay on track with your savings goals" },
            NotificationChannel(
                MONTHLY_SUMMARY,
                "Monthly Summary",
                NotificationManager.IMPORTANCE_DEFAULT
            )
                .apply { description = "Your monthly financial summary" },
            NotificationChannel(
                BILL_REMINDERS,
                "Bill Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply { description = "Reminders for upcoming recurring bills" }
        ).forEach { manager.createNotificationChannel(it) }
    }

    companion object {
        const val BUDGET_ALERTS = "nexledger_budget_alerts"
        const val GOAL_REMINDERS = "nexledger_goal_reminders"
        const val MONTHLY_SUMMARY = "nexledger_monthly_summary"
        const val BILL_REMINDERS = "nexledger_bill_reminders"
    }
}
