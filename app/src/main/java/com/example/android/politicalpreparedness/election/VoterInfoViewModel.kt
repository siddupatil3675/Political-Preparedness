package com.example.android.politicalpreparedness.election

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.ElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.model.Election
import com.example.android.politicalpreparedness.model.Result
import kotlinx.coroutines.launch

class VoterInfoViewModel(private val dataSource: ElectionDataSource) : BaseViewModel() {

    //live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo

    private var _election = MutableLiveData<Election>()
    val election: LiveData<Election>
        get() = _election


    fun getVoterInfo() {
        viewModelScope.launch {
            when (val result = dataSource.getVoterInfo(election.value!!)) {
                is Result.Success -> {
                    _voterInfo.postValue(result.data)
                }
                is Result.Error -> {
                    showSnackBar.postValue("No details found, $result.data")
                }
            }
        }
    }

    fun setElection(election: Election) {
        _election.value = election
    }

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
    fun updateElection(election: Election) {
        election.isSaved = !election.isSaved
        viewModelScope.launch {
            dataSource.updateElection(election)
            setElection(election)
        }
    }

}