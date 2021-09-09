package com.example.oldlady.score

import android.app.Application
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.oldlady.R
import com.example.oldlady.convertLongToDateString
import com.example.oldlady.convertLongToElapsedTimeString
import com.example.oldlady.convertLongToElapsedTimeString2
import com.example.oldlady.databinding.ListItemScoreBinding

class ScoreAdapter: ListAdapter<Score, ScoreAdapter.ViewHolder>(ScoreDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding: ListItemScoreBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Score) {
            binding.score = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemScoreBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class ScoreDiffCallback: DiffUtil.ItemCallback<Score>() {
    override fun areItemsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem.scoreId == newItem.scoreId
    }

    override fun areContentsTheSame(oldItem: Score, newItem: Score): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("player1Name")
fun TextView.setPlayer1Name(item: Score?) {
    item?.let {
        text = item.player1Name
    }
}

@BindingAdapter("player2Name")
fun TextView.setPlayer2Name(item: Score?) {
    item?.let {
        text = item.player2Name
    }
}

@BindingAdapter("player1Score")
fun TextView.setPlayer1Score(item: Score?) {
    item?.let {
        text = item.player1Score.toString()
    }
}

@BindingAdapter("player2Score")
fun TextView.setPlayer2Score(item: Score?) {
    item?.let {
        text = item.player2Score.toString()
    }
}

@BindingAdapter("playedOnText")
fun TextView.setPlayedOnText(item: Score?) {
    item?.let {
        text = convertLongToDateString(item.playedOn)
    }
}

@BindingAdapter("elapsedTimeText")
fun TextView.setElapsedTimeText(item: Score?) {
    item?.let {
        val elapsedTimeText = convertLongToElapsedTimeString2(item.elapsedTime)
        if (elapsedTimeText != null) {
            text = elapsedTimeText
        } else {
            setText(R.string.not_finished)
        }
    }
}