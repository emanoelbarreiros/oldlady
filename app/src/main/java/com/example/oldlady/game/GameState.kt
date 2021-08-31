package com.example.oldlady.game

enum class GameState {
    IDLE, O_TURN, X_TURN, X_WIN, O_WIN;

    fun opposite(): GameState {
        if (this == O_TURN) {
            return X_TURN
        } else if (this == X_TURN){
            return O_TURN
        }
        return IDLE
    }
}