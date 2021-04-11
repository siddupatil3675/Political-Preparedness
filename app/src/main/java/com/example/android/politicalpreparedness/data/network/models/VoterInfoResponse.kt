package com.example.android.politicalpreparedness.data.network.models

import com.example.android.politicalpreparedness.model.Election
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class VoterInfoResponse (
        val election: Election,
        val pollingLocations: String? = null, //TODO: Future Use
        val contests: String? = null, //TODO: Future Use
        val state: List<State>? = null,
        val electionElectionOfficials: List<ElectionOfficial>? = null
)