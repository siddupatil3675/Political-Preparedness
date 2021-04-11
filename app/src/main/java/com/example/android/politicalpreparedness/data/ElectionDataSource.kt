package com.example.android.politicalpreparedness.data

import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.model.Election
import com.example.android.politicalpreparedness.model.Result
import com.example.android.politicalpreparedness.representative.model.Representative

interface ElectionDataSource {
    val followedElections: LiveData<List<Election>>
    val upcomingElections: LiveData<List<Election>>

    suspend fun fetchElectionsData()

    suspend fun updateElection(election: Election)

    suspend fun getVoterInfo(election: Election): Result<VoterInfoResponse>

    suspend fun getRepresentatives(address: Address): Result<List<Representative>>
}