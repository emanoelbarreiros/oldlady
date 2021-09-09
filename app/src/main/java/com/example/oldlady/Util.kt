package com.example.oldlady

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.core.text.HtmlCompat
import com.example.oldlady.score.Score
import java.text.SimpleDateFormat

/**
 * Take the Long milliseconds returned by the system and stored in Room,
 * and convert it to a nicely formatted string for display.
 *
 * EEEE - Display the long letter version of the weekday
 * MMM - Display the letter abbreviation of the nmotny
 * dd-yyyy - day in month and full year numerically
 * HH:mm - Hours and minutes in 24hr format
 */
@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE dd-MMM-yyyy HH:mm")
        .format(systemTime).toString()
}

fun convertLongToElapsedTimeString(elapsedTime: Long, resources: Resources): String {
    if (elapsedTime == Long.MAX_VALUE) {
        return resources.getString(R.string.not_finished)
    } else {
        var seconds = elapsedTime / 1000
        var minutes = 0L
        var hours = 0L

        if (seconds >= 60) {
            minutes = seconds / 60
            seconds %= 60
        }

        if (minutes >= 60) {
            hours = minutes / 60
            minutes %= 60
        }

        if (hours > 0) {
            return "$hours h:$minutes m:$seconds s"
        } else if (minutes > 0) {
            return "$minutes m:$seconds s"
        } else {
            return "$seconds s"
        }
    }
}

fun convertLongToElapsedTimeString2(elapsedTime: Long): String? {
    if (elapsedTime == Long.MAX_VALUE) {
        return null
    } else {
        var seconds = elapsedTime / 1000
        var minutes = 0L
        var hours = 0L

        if (seconds >= 60) {
            minutes = seconds / 60
            seconds %= 60
        }

        if (minutes >= 60) {
            hours = minutes / 60
            minutes %= 60
        }

        if (hours > 0) {
            return "$hours h:$minutes m:$seconds s"
        } else if (minutes > 0) {
            return "$minutes m:$seconds s"
        } else {
            return "$seconds s"
        }
    }
}

fun formatScores(scores: List<Score>, resources: Resources): Spanned {
    val sb = StringBuilder()
    if (scores.isNotEmpty()) {
        sb.apply {
            scores.forEach {
                append("<br>")
                append(resources.getString(R.string.played_on_formatted))
                append("\t${convertLongToDateString(it.playedOn)}<br>")
                append(resources.getString(R.string.game_length_formatted))
                append("\t${convertLongToElapsedTimeString(it.elapsedTime, resources)}<br>")
                append(resources.getString(R.string.player_1_formatted))
                append("\t${it.player1Name}<br>")
                append(resources.getString(R.string.player_2_formatted))
                append("\t${it.player2Name}<br>")
                append(resources.getString(R.string.player_1_score_formatted))
                append("\t${it.player1Score}<br>")
                append(resources.getString(R.string.player_2_score_formatted))
                append("\t${it.player2Score}<br><br>")
            }
        }
    } else {
        sb.append("<br>")
        sb.append(resources.getString(R.string.no_records))
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY)
    } else {
        HtmlCompat.fromHtml(sb.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
    }
}