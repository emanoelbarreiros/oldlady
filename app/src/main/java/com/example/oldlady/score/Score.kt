package com.example.oldlady.score

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class Score(

    @PrimaryKey(autoGenerate = true)
    var scoreId: Long = 0L,

    @ColumnInfo(name = "player_1_name")
    var player1Name: String,

    @ColumnInfo(name = "player_2_name")
    var player2Name: String,

    @ColumnInfo(name = "player_1_score")
    var player1Score: Int = 0,

    @ColumnInfo(name = "player_2_score")
    var player2Score: Int = 0,

    @ColumnInfo(name = "played_on")
    var playedOn: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "elapsed_time")
    var elapsedTime: Long = Long.MAX_VALUE
)