package com.hgabriel.gamemetascore.sync

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.ForegroundInfo
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.GameRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncGameWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted params: WorkerParameters,
    private val repository: GameRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        repository.sync()
        Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo =
        ForegroundInfo(SyncNotificationId, getNotification())

    private fun getNotification() =
        NotificationCompat.Builder(context, SyncNotificationChannelID)
            .setSmallIcon(R.drawable.ic_videogame_24)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.sync_games_background))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

    companion object {
        private const val SyncNotificationId = 0
        private const val SyncNotificationChannelID = "SyncNotificationChannel"

        fun sync(context: Context) {
            val syncConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = OneTimeWorkRequestBuilder<SyncGameWorker>()
                .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                .setConstraints(syncConstraints)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "SyncGameWorker",
                ExistingWorkPolicy.KEEP,
                request
            )
        }
    }
}
