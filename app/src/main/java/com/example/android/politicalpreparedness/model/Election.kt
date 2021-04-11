package com.example.android.politicalpreparedness.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.data.datatbase.entity.DatabaseElection
import com.example.android.politicalpreparedness.data.network.models.Division
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity(tableName = "election_table")

@Parcelize
data class Election(
        @PrimaryKey val id: Int,
        @ColumnInfo(name = "name") val name: String,
        @ColumnInfo(name = "electionDay") val electionDay: Date,
        @ColumnInfo(name = "isSaved") var isSaved: Boolean = false,
        @Embedded(prefix = "division_") @Json(name = "ocdDivisionId") val division: Division
) : Parcelable

fun Election.asDatabaseModel(): DatabaseElection {
    return DatabaseElection(
            id = this.id,
            name = this.name,
            division = this.division,
            electionDay = this.electionDay,
            isSaved = this.isSaved
    )
}