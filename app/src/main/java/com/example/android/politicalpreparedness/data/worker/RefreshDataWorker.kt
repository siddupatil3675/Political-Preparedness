package com.example.android.politicalpreparedness.data.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.politicalpreparedness.data.ElectionDataSource
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class RefreshDataWorker(
        appContext: Context,
        parameters: WorkerParameters): CoroutineWorker(appContext, parameters), KoinComponent {

    private val civicsRepository: ElectionDataSource by inject()

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        return try {
            civicsRepository.fetchElectionsData()
            Result.success()

        }catch (exception: Exception){
            Timber.e("$exception")
            Result.retry()
        }
    }
}