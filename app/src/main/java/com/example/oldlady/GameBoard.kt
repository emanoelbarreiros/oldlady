package com.example.oldlady

class GameBoard {
    private val board = mutableListOf<MutableList<Play>>(
        mutableListOf(Play.NONE,Play.NONE,Play.NONE),
        mutableListOf(Play.NONE,Play.NONE,Play.NONE),
        mutableListOf(Play.NONE,Play.NONE,Play.NONE)
    )

    fun play(line: Int, column: Int, play: Play): Boolean {
        if (board[line][column] == Play.NONE) {
            board[line][column] = play
            return true
        } else {
            return false
        }
    }

    fun getPlay(line: Int, column: Int): Play = board[line][column]

    fun getLine(line: Int): List<Play> = board[line]

    fun getColumn(column: Int): List<Play> =
        listOf(board[0][column], board[1][column], board[2][column])

    fun getMainDiagonal(): List<Play> = listOf(board[0][0], board[1][1], board[2][2])

    fun getSecondaryDiagonal(): List<Play> = listOf(board[0][2], board[1][1], board[2][0])

    fun getLinesColumnsDiagonals() = listOf(getLine(0), getLine(1), getLine(2),
            getColumn(0), getColumn(1), getColumn(2),
            getMainDiagonal(), getSecondaryDiagonal())
}