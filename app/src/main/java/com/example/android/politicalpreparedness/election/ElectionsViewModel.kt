package com.example.android.politicalpreparedness.election

import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.ElectionDataSource
import kotlinx.coroutines.launch

class ElectionsViewModel(private val electionDataSource: ElectionDataSource) : BaseViewModel() {

    val upcomingElections = electionDataSource.upcomingElections
    val followedElections = electionDataSource.followedElections

    init {
        viewModelScope.launch {
            try {
                electionDataSource.fetchElectionsData()
            } catch (e: Exception) {
                showSnackBar.postValue("Couldn't connect, check your network")
            }
        }
    }
}