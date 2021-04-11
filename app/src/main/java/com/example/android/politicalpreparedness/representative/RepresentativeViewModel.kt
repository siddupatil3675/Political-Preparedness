package com.example.android.politicalpreparedness.representative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.base.BaseViewModel
import com.example.android.politicalpreparedness.data.ElectionDataSource
import com.example.android.politicalpreparedness.data.network.models.Address
import com.example.android.politicalpreparedness.model.Result
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel(
        private val dataSource: ElectionDataSource
) : BaseViewModel() {
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    //function get address from geo location
    fun setAddressFromGeoLocation(address: Address) {
        _address.value = address
    }

    fun getRepresentatives() {
        showLoading.value = true
        viewModelScope.launch {
            val result = dataSource.getRepresentatives(address.value!!)
            showLoading.value = false
            when (result) {
                is Result.Success -> {
                    _representatives.postValue(result.data)

                }
                is Result.Error -> {
                    showSnackBar.value = "could not get representatives for the address"
                }
            }

        }
    }

    // function to get address from individual fields
    fun getAddressFromIndividualFields(address: Address) {
        _address.value = address
    }

}
