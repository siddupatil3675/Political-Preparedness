package com.example.android.politicalpreparedness

import android.app.Application
import androidx.work.*
import com.example.android.politicalpreparedness.data.ElectionDataSource
import com.example.android.politicalpreparedness.data.ElectionRepository
import com.example.android.politicalpreparedness.data.datatbase.LocalDB
import com.example.android.politicalpreparedness.data.worker.RefreshDataWorker
import com.example.android.politicalpreparedness.election.ElectionsViewModel
import com.example.android.politicalpreparedness.election.VoterInfoViewModel
import com.example.android.politicalpreparedness.representative.RepresentativeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        /**
         * use Koin Library as a service locator
         */
        val myModule = module {
            //Declare a ViewModel - be later inject into Fragment with dedicated injector using by viewModel()
            viewModel {
                ElectionsViewModel(get() as ElectionDataSource)
            }

            viewModel {
                VoterInfoViewModel(get() as ElectionDataSource)
            }

            viewModel {
                RepresentativeViewModel(get() as ElectionDataSource)
            }


            // Repository
            single {
                ElectionRepository(
                        get()
                ) as ElectionDataSource

            }
            single {
                LocalDB.createElectionDao(this@MyApp)
            }
        }

        startKoin {
            androidContext(this@MyApp)
            modules(listOf(myModule))
        }
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresCharging(true)
                .setRequiresBatteryNotLow(true)
                .setRequiresDeviceIdle(true)
                .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)
    }
}