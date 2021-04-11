package com.example.android.politicalpreparedness.data.network.models

import com.example.android.politicalpreparedness.data.network.models.Office
import com.example.android.politicalpreparedness.data.network.models.Official

data class RepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
)