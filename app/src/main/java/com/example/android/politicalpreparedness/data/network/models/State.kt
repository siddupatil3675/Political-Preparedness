package com.example.android.politicalpreparedness.data.network.models

import com.example.android.politicalpreparedness.data.network.models.AdministrationBody

data class State (
    val name: String,
    val electionAdministrationBody: AdministrationBody
)