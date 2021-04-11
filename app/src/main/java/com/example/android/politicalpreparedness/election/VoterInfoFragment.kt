package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.base.BaseFragment
import com.example.android.politicalpreparedness.data.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import org.koin.android.ext.android.inject

class VoterInfoFragment : BaseFragment() {

    override val _viewModel: VoterInfoViewModel by inject()

    private lateinit var binding: FragmentVoterInfoBinding

    private val args: VoterInfoFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentVoterInfoBinding.inflate(inflater)

        binding.viewModel = _viewModel

        val election = args.argElection

        binding.election = election

        _viewModel.setElection(election)

        if (isNetworkConnected()) {
            _viewModel.getVoterInfo()
        } else {
            _viewModel.showSnackBar.postValue(getString(R.string.no_internet_connecton))
        }

        _viewModel.election.observe(viewLifecycleOwner, Observer {
            binding.followElectionBtn.text = if (!election.isSaved) getString(R.string.follow_election) else getString(R.string.unfollow_election)
        })

        //Populate voter info -- hide views without provided data.
        _viewModel.voterInfo.observe(viewLifecycleOwner, Observer {
            validateAndDisplayVoterInfo(it)
        })
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
         */

        binding.followElectionBtn.setOnClickListener {
            _viewModel.updateElection(election)
        }

        return binding.root

    }

    private fun validateAndDisplayVoterInfo(infoResponse: VoterInfoResponse?) {
        infoResponse?.state?.firstOrNull()?.electionAdministrationBody.let { body ->
            binding.stateBallot.setOnClickListener {
                openUrl(body?.ballotInfoUrl)
            }

            binding.stateLocations.setOnClickListener {
                openUrl(body?.votingLocationFinderUrl)
            }

            binding.address.text = body?.correspondenceAddress?.toFormattedString()
        }
    }

    //method to load URL intents
    private fun openUrl(url: String?) {
        url?.let { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) }
    }
}