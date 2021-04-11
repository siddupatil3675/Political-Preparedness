package com.example.android.politicalpreparedness.data.datatbase

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.politicalpreparedness.data.datatbase.entity.DatabaseElection

@Dao
interface ElectionDao {

    //Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(vararg electionEntity: DatabaseElection)

    // Add select all election query
    @Query("SELECT * FROM ELECTION_TABLE")
    fun getAllElections(): LiveData<List<DatabaseElection>>

    //Add select single election query
    @Query("SELECT * FROM ELECTION_TABLE where id = :id")
    fun getElectionById(id: Int): LiveData<DatabaseElection>

    // Add select single election query
    @Query("SELECT * FROM ELECTION_TABLE where isSaved = 1")
    fun getFollowedElections(): LiveData<List<DatabaseElection>>

    //update election query
    @Update
    fun updateElection(electionEntity: DatabaseElection)

    //Add delete query
    @Delete
    suspend fun delete(electionEntity: DatabaseElection)

    //Add clear query
    @Query("DELETE FROM ELECTION_TABLE")
    suspend fun clear()

}