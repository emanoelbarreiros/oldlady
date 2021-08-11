package com.example.oldlady

import android.app.ActivityManager
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.example.oldlady.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var state: GameState = GameState.IDLE
    private var bottomPlayerPoints = 0
    private var topPlayerPoints = 0
    private var board: GameBoard = GameBoard()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setListeners()
        updateViews()
    }

    private fun setListeners() {
        binding.startButton.setOnClickListener { handleGameStart() }
        binding.resetButton.setOnClickListener { handleGameReset() }
        binding.continueButton.setOnClickListener { handleGameContinue() }

        var playViews: List<View>
        binding.apply {
            playViews = listOf(pos00Text, pos01Text, pos02Text,
                pos10Text, pos11Text, pos12Text,
                pos20Text, pos21Text, pos22Text)
        }

        for (view in playViews) {
            view.setOnClickListener { handlePlay(it) }
        }
    }

    private fun handlePlay(view: View) {
        if (state == GameState.O_TURN || state == GameState.X_TURN) {
            val validPlay = when (view.id) {
                R.id.pos_0_0_text -> board.play(0, 0, getPlay(state))
                R.id.pos_0_1_text -> board.play(0, 1, getPlay(state))
                R.id.pos_0_2_text -> board.play(0, 2, getPlay(state))
                R.id.pos_1_0_text -> board.play(1, 0, getPlay(state))
                R.id.pos_1_1_text -> board.play(1, 1, getPlay(state))
                R.id.pos_1_2_text -> board.play(1, 2, getPlay(state))
                R.id.pos_2_0_text -> board.play(2, 0, getPlay(state))
                R.id.pos_2_1_text -> board.play(2, 1, getPlay(state))
                R.id.pos_2_2_text -> board.play(2, 2, getPlay(state))
                else -> false
            }

            if (validPlay) {
                val winner: Play = checkWinningCondition()
                if (winner == Play.X) {
                    state = GameState.X_WIN
                } else if (winner == Play.O) {
                    state = GameState.O_WIN
                } else {
                    transitionState()
                }
                updateViews()
            }
        }
    }

    private fun checkWinningCondition(): Play {
        val lines = board.getLinesColumnsDiagonals()

        for (check in lines) {
            for (play in listOf(Play.X, Play.O)) {
                if (check.all { it == play }) {
                    if (play == Play.X) {
                        bottomPlayerPoints += 1
                    } else {
                        topPlayerPoints += 1
                    }
                    return play
                }
            }
        }
        return Play.NONE
    }

    private fun transitionState() {
        state = state.opposite()
    }

    private fun updateViews() {
        //binding.invalidateAll()
        if (state == GameState.X_TURN) {
            binding.bottomPlayerInfoText.setText(R.string.play_turn)
            binding.topPlayerInfoText.setText(R.string.wait_turn)
            binding.startButton.visibility = View.GONE
            binding.resetButton.visibility = View.VISIBLE
            binding.continueButton.visibility = View.GONE
        } else if (state == GameState.O_TURN) {
            binding.bottomPlayerInfoText.setText(R.string.wait_turn)
            binding.topPlayerInfoText.setText(R.string.play_turn)
            binding.startButton.visibility = View.GONE
            binding.resetButton.visibility = View.VISIBLE
            binding.continueButton.visibility = View.GONE
        } else if (state == GameState.X_WIN) {
            binding.bottomPlayerInfoText.setText(R.string.you_won)
            binding.topPlayerInfoText.setText(R.string.you_lost)
            binding.resetButton.visibility = View.VISIBLE
            binding.continueButton.visibility = View.VISIBLE
        } else if (state == GameState.O_WIN) {
            binding.bottomPlayerInfoText.setText(R.string.you_lost)
            binding.topPlayerInfoText.setText(R.string.you_won)
            binding.resetButton.visibility = View.VISIBLE
            binding.continueButton.visibility = View.VISIBLE
        }

        if (state == GameState.IDLE) {
            binding.startButton.visibility = View.VISIBLE
            binding.resetButton.visibility = View.GONE
            binding.continueButton.visibility = View.GONE
            binding.topPlayerInfoText.setText(R.string.waiting)
            binding.bottomPlayerInfoText.setText(R.string.waiting)
        }

        binding.apply {
            topPlayerPointsText.text = topPlayerPoints.toString()
            bottomPlayerPointsText.text = bottomPlayerPoints.toString()

            pos00Text.text = getPlayText(board.getPlay(0,0))
            pos01Text.text = getPlayText(board.getPlay(0,1))
            pos02Text.text = getPlayText(board.getPlay(0,2))
            pos10Text.text = getPlayText(board.getPlay(1,0))
            pos11Text.text = getPlayText(board.getPlay(1,1))
            pos12Text.text = getPlayText(board.getPlay(1,2))
            pos20Text.text = getPlayText(board.getPlay(2,0))
            pos21Text.text = getPlayText(board.getPlay(2,1))
            pos22Text.text = getPlayText(board.getPlay(2,2))
        }
    }

    private fun getPlayText(play: Play): String {
        return when(play) {
            Play.X -> "X"
            Play.O -> "O"
            else -> "-"
        }
    }

    private fun getPlay(state: GameState): Play {
        return when(state) {
            GameState.X_TURN -> Play.X
            GameState.O_TURN -> Play.O
            else -> Play.NONE
        }
    }

    private fun handleGameStart() {
        state = GameState.X_TURN
        updateViews()
    }

    private fun handleGameReset() {
        state = GameState.IDLE
        topPlayerPoints = 0
        bottomPlayerPoints = 0
        board = GameBoard()
        updateViews()
    }

    private fun handleGameContinue() {
        state = GameState.X_TURN
        board = GameBoard()
        updateViews()
    }
}