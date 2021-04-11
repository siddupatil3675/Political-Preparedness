package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.data.datatbase.ElectionDao
import com.example.android.politicalpreparedness.data.datatbase.entity.asDomainModel
import com.example.android.politicalpreparedness.data.network.CivicsApi
import com.example.android.politicalpreparedness.data.network.asDatabaseModel
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.model.Election
import com.example.android.politicalpreparedness.model.Result
import com.example.android.politicalpreparedness.model.asDatabaseModel
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class ElectionRepository(
        private val electionDao: ElectionDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ElectionDataSource {

    override val followedElections: LiveData<List<Election>> =
            Transformations.map(electionDao.getFollowedElections()) {
                it.asDomainModel()
            }
    override val upcomingElections: LiveData<List<Election>> =
            Transformations.map(
                    electionDao.getAllElections()) {
                it.asDomainModel()
            }

    override suspend fun fetchElectionsData() {
        withContext(ioDispatcher) {
            try {
                val response = CivicsApi.retrofitService.getElections()
                val elections = response.elections.asDatabaseModel()
                electionDao.insertElections(*elections)
                Timber.d("refresh success")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Timber.d("refresh failed ${e.message}")
                }
            }
        }
    }

    override suspend fun updateElection(election: Election) {
        withContext(ioDispatcher) {
            try {
                electionDao.updateElection(election.asDatabaseModel())
            } catch (e: Exception) {
                Timber.d("SOmething wroong")
            }
        }
    }

    override suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse> = withContext(ioDispatcher){
        try{
            val voterInfo = CivicsApi.retrofitService.getVoterInfo(election.division.toFormattedString(), election.id)
            Result.Success(voterInfo)
        }
        catch (exception: Exception){
            Result.Error("Error getting voter info")
        }
    }

    override suspend fun getRepresentatives(address: Address): Result<List<Representative>> = withContext(ioDispatcher) {
        try {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentatives(address.toFormattedString())
            Result.Success(offices.flatMap { it.getRepresentatives(officials) })
        } catch (exception: java.lang.Exception) {
            Result.Error("Error getting representatives")
        }
    }

}