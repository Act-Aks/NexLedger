package com.actaks.nexledger.feature.notifications

import android.Manifest
import android.R
import android.app.PendingIntent
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.actaks.nexledger.core.database.repository.BudgetRepository
import com.actaks.nexledger.core.database.repository.GoalRepository
import com.actaks.nexledger.core.database.repository.RecurringTransactionRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Calendar

/**
 * Daily background worker using Koin for dependency injection.
 *
 * Implements [KoinComponent] so it can resolve dependencies lazily.
 */
class DailyCheckWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val budgetRepository: BudgetRepository by inject()
    private val goalRepository: GoalRepository by inject()
    private val transactionRepository: TransactionRepository by inject()
    private val recurringTransactionRepository: RecurringTransactionRepository by inject()

    override suspend fun doWork(): Result {
        if (!hasNotificationPermission()) return Result.success()
        checkBudgets()
        checkGoals()
        checkRecurring()
        return Result.success()
    }

    private suspend fun checkBudgets() {
        val now = Calendar.getInstance()
        val startOfMonth = now.clone() as Calendar
        startOfMonth.set(Calendar.DAY_OF_MONTH, 1); startOfMonth.set(Calendar.HOUR_OF_DAY, 0)
        startOfMonth.set(Calendar.MINUTE, 0); startOfMonth.set(
            Calendar.SECOND,
            0
        ); startOfMonth.set(Calendar.MILLISECOND, 0)

        val totalExpenses = transactionRepository.sumByTypeAndDateRange(
            TransactionType.EXPENSE, startOfMonth.timeInMillis, now.timeInMillis
        )
        val budgets = budgetRepository.getAll()
        collectFirst(budgets) { budgetList ->
            budgetList.forEach { budget ->
                if (totalExpenses >= budget.amount * 0.9) {
                    val title =
                        if (totalExpenses >= budget.amount) "Budget exceeded!" else "Budget alert"
                    val body = "You've spent ₹%.0f of your ₹%.0f budget.".format(
                        totalExpenses,
                        budget.amount
                    )
                    showNotification(
                        NotificationChannels.BUDGET_ALERTS,
                        budget.id.toInt(),
                        title,
                        body
                    )
                }
            }
        }
    }

    private suspend fun checkGoals() {
        val goals = goalRepository.getAll()
        val now = System.currentTimeMillis()
        val weekFromNow = now + 7 * 24 * 60 * 60 * 1000L
        collectFirst(goals) { goalList ->
            goalList.filter { it.deadline in now..weekFromNow && it.currentAmount < it.targetAmount }
                .forEach { goal ->
                    val remaining = goal.targetAmount - goal.currentAmount
                    showNotification(
                        NotificationChannels.GOAL_REMINDERS, goal.id.toInt(),
                        "Goal deadline approaching",
                        "₹%.0f remaining for \"%s\"".format(remaining, goal.name)
                    )
                }
        }
    }

    private suspend fun checkRecurring() {
        val now = System.currentTimeMillis()
        val tomorrow = now + 24 * 60 * 60 * 1000L
        val recurrings = recurringTransactionRepository.getAll()
        collectFirst(recurrings) { recurringTransactions ->
            recurringTransactions.filter { it.nextExecution in now..tomorrow }.forEach { rec ->
                showNotification(
                    NotificationChannels.BILL_REMINDERS, rec.id.toInt(),
                    "Bill due soon",
                    "A recurring payment of ₹%.0f is due.".format(rec.amount)
                )
            }
        }
    }

    private fun showNotification(channelId: String, id: Int, title: String, body: String) {
        val intent = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        NotificationManagerCompat.from(applicationContext).notify(id, notification)
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 33) {
            ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else true
    }

    private suspend fun <T> collectFirst(
        flow: Flow<T>,
        block: suspend (T) -> Unit
    ) {
        try {
            flow.collect { block(it); throw kotlinx.coroutines.CancellationException("done") }
        } catch (_: CancellationException) {
        }
    }
}
