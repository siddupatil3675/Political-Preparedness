package com.example.android.politicalpreparedness.data.datatbase.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.data.network.models.Division
import com.example.android.politicalpreparedness.model.Election
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "election_table")
@Parcelize
data class DatabaseElection(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @Embedded(prefix = "division_") val division: Division,
        @ColumnInfo(name = "isSaved") val isSaved: Boolean
) : Parcelable

fun List<DatabaseElection>.asDomainModel(): List<Election> {
    return map {
        Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                isSaved = it.isSaved,
                division = it.division
        )
    }
}