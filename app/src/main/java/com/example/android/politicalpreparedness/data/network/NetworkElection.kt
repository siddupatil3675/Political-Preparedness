package com.example.android.politicalpreparedness.data.network

import com.example.android.politicalpreparedness.data.datatbase.entity.DatabaseElection
import com.example.android.politicalpreparedness.data.network.models.Division
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*


@JsonClass(generateAdapter = true)
data class NetworkElection(
        val id: Int,
        val name: String,
        val electionDay: Date,
        @Json(name = "ocdDivisionId") val division: Division
)

//Convert network results to domain objects

//Convert data transfer objects(network objects) to database objects
fun List<NetworkElection>.asDatabaseModel(): Array<DatabaseElection> {
    return this.map {
        DatabaseElection(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                isSaved = false,
                division = it.division
        )
    }.toTypedArray()
}