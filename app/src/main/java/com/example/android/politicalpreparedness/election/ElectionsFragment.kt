package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.base.NavigationCommand
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.model.Election
import org.koin.android.ext.android.inject

class ElectionsFragment : BaseFragment() {

    override val _viewModel: ElectionsViewModel by inject()

    private lateinit var binding: FragmentElectionBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = _viewModel
        binding.upcomingRv.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            navigateToVoterInfo(it)
        })
        binding.followedRv.adapter = ElectionListAdapter(ElectionListAdapter.ElectionListener {
            navigateToVoterInfo(it)
        })

        if (!isNetworkConnected()) {
            _viewModel.showSnackBar.postValue(getString(R.string.no_internet_connecton))
        }
        return binding.root

    }

    private fun navigateToVoterInfo(election: Election) {
        _viewModel.navigationCommand.postValue(
                NavigationCommand.To(
                        ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(
                                election)
                )
        )
    }
}