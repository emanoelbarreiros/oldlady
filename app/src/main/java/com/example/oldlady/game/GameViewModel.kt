package com.example.oldlady.game

import android.app.Application
import androidx.lifecycle.*
import com.example.oldlady.R
import com.example.oldlady.score.Score
import com.example.oldlady.score.ScoreDatabaseDao
import kotlinx.coroutines.*

class GameViewModel(val database: ScoreDatabaseDao, application: Application) : AndroidViewModel(application)  {

    private var _currentScore: Score? = null

    var player1Name =  MutableLiveData<String>()
    var player2Name = MutableLiveData<String>()

    private var _bottomPlayerPoints = MutableLiveData<Int>(0)
    val bottomPlayerPoints: LiveData<Int>
        get() = _bottomPlayerPoints

    private var _topPlayerPoints = MutableLiveData<Int>(0)
    val topPlayerPoints: LiveData<Int>
        get() = _topPlayerPoints

    private var _state = MutableLiveData<GameState>(GameState.X_TURN)
    val state: LiveData<GameState>
        get() = _state

    private var _gameBoard = MutableLiveData<GameBoard>(GameBoard())
    val gameBoard: LiveData<GameBoard>
        get() = _gameBoard

    val boardPos00 = Transformations.map(gameBoard) { getPlayText(it.getPlay(0,0)) }
    val boardPos01 = Transformations.map(gameBoard) { getPlayText(it.getPlay(0,1)) }
    val boardPos02 = Transformations.map(gameBoard) { getPlayText(it.getPlay(0,2)) }
    val boardPos10 = Transformations.map(gameBoard) { getPlayText(it.getPlay(1,0)) }
    val boardPos11 = Transformations.map(gameBoard) { getPlayText(it.getPlay(1,1)) }
    val boardPos12 = Transformations.map(gameBoard) { getPlayText(it.getPlay(1,2)) }
    val boardPos20 = Transformations.map(gameBoard) { getPlayText(it.getPlay(2,0)) }
    val boardPos21 = Transformations.map(gameBoard) { getPlayText(it.getPlay(2,1)) }
    val boardPos22 = Transformations.map(gameBoard) { getPlayText(it.getPlay(2,2)) }

    private var _bottomPlayerInfoText = MutableLiveData<Int>(R.string.waiting)
    val bottomPlayerInfoText: LiveData<Int>
        get() = _bottomPlayerInfoText

    private var _topPlayerInfoText = MutableLiveData<Int>(R.string.waiting)
    val topPlayerInfoText: LiveData<Int>
        get() = _topPlayerInfoText

    val gameRunning: MutableLiveData<Boolean> = MutableLiveData<Boolean>(false)

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun incrementTopPlayerPoints() {
        _topPlayerPoints.value = _topPlayerPoints.value?.inc()
    }

    fun incrementBottomPlayerPoints() {
        _bottomPlayerPoints.value = _bottomPlayerPoints.value?.inc()
    }

    fun resetPoints() {
        _bottomPlayerPoints.value = 0
        _topPlayerPoints.value = 0
    }

    fun executePlay(line: Int, column: Int) {
        if (_state.value == GameState.O_TURN || _state.value == GameState.X_TURN) {
            val validPlay = _gameBoard.value!!.play(line, column, getPlay(_state.value!!))

            if (validPlay) {
                _gameBoard.postValue(_gameBoard.value)
                val winner: Play = checkWinningCondition()
                if (winner == Play.X) {
                    _state.value = GameState.X_WIN
                } else if (winner == Play.O) {
                    _state.value = GameState.O_WIN
                } else {
                    transitionState()
                }
                updateBindings()
            }
        }
    }

    private fun getPlay(state: GameState): Play {
        return when(state) {
            GameState.X_TURN -> Play.X
            GameState.O_TURN -> Play.O
            else -> Play.NONE
        }
    }

    private fun transitionState() {
        _state.value = _state.value!!.opposite()
    }

    private fun checkWinningCondition(): Play {
        val lines = _gameBoard.value!!.getLinesColumnsDiagonals()

        for (check in lines) {
            for (play in listOf(Play.X, Play.O)) {
                if (check.all { it == play }) {
                    if (play == Play.X) {
                        incrementBottomPlayerPoints()
                        _currentScore?.player1Score?.plus(1)
                    } else {
                        incrementTopPlayerPoints()
                        _currentScore?.player2Score?.plus(1)
                    }
                    updateScore()
                    return play
                }
            }
        }
        return Play.NONE
    }

    private fun getPlayText(play: Play): String {
        return when(play) {
            Play.X -> "X"
            Play.O -> "O"
            else -> "-"
        }
    }

    private fun updateBindings() {
        //binding.invalidateAll()
        if (_state.value == GameState.X_TURN) {
            _bottomPlayerInfoText.value = R.string.play_turn
            _topPlayerInfoText.value = R.string.wait_turn
        } else if (_state.value == GameState.O_TURN) {
            _bottomPlayerInfoText.value = R.string.wait_turn
            _topPlayerInfoText.value = R.string.play_turn
        } else if (_state.value == GameState.X_WIN) {
            _bottomPlayerInfoText.value = R.string.you_won
            _topPlayerInfoText.value = R.string.you_lost
            gameRunning.value = false
        } else if (_state.value == GameState.O_WIN) {
            _bottomPlayerInfoText.value = R.string.you_lost
            _topPlayerInfoText.value = R.string.you_won
            gameRunning.value = false
        }
    }

    fun continueGame() {
        _state.value = GameState.X_TURN
        _gameBoard.value = GameBoard()
        gameRunning.value = true
    }

    fun resetGame() {
        _state.value = GameState.X_TURN
        resetPoints()
        _gameBoard.value = GameBoard()
        gameRunning.value = true
        recordNewScore()
    }

    fun startGame() {
        gameRunning.value = true
        _state.value = GameState.X_TURN
        recordNewScore()
        updateBindings()
    }

    private fun updateScore() {
        uiScope.launch {
            val startTime = _currentScore!!.playedOn
            _currentScore!!.elapsedTime = System.currentTimeMillis() - startTime
            _currentScore!!.player1Score = bottomPlayerPoints.value!!
            _currentScore!!.player2Score = topPlayerPoints.value!!
            updateCurrentScore()
        }
    }

    private suspend fun updateCurrentScore() {
        withContext(Dispatchers.IO) {
            database.update(_currentScore!!)
        }
    }

    private fun recordNewScore() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                val score = Score(player1Name = player1Name.value!!, player2Name = player2Name.value!!)
                database.insert(score)
                _currentScore = database.getLastScore()
            }
        }

    }
}