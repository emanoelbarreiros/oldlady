package com.example.oldlady.score

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ScoreDatabaseDao {

    @Insert
    fun insert(score: Score)

    @Query("SELECT * FROM score_table ORDER BY scoreId DESC")
    fun getAllScores(): LiveData<List<Score>>

    @Query("SELECT * FROM score_table ORDER BY scoreId DESC LIMIT 1")
    fun getLastScore(): Score?

    @Query("DELETE FROM score_table")
    fun clear()

    @Update
    fun update(score: Score)

}