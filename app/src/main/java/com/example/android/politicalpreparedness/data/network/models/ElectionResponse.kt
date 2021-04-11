package com.example.android.politicalpreparedness.data.network.models

import com.example.android.politicalpreparedness.data.network.NetworkElection
import com.example.android.politicalpreparedness.model.Election
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ElectionResponse(
        val kind: String,
        val elections: List<NetworkElection>
)