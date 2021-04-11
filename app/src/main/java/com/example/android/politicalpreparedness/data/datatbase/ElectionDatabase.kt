package com.example.android.politicalpreparedness.data.datatbase

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.data.datatbase.entity.DatabaseElection

@Database(entities = [DatabaseElection::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase : RoomDatabase() {
    abstract val electionDao: ElectionDao
}