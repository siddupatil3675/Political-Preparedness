package com.example.android.politicalpreparedness.data.datatbase

import android.content.Context
import androidx.room.Room

object LocalDB {
    /**
     * static method that creates a election class and returns the DAO of the election
     */
    fun createElectionDao(context: Context): ElectionDao {
        return Room.databaseBuilder(
                context.applicationContext,
                ElectionDatabase::class.java,
                "election_database"
        )
                .build().electionDao
    }
}