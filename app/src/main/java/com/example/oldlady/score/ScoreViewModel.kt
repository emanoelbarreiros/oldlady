package com.example.oldlady.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.oldlady.formatScores
import kotlinx.coroutines.*

class ScoreViewModel(val database: ScoreDatabaseDao, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    var scores = database.getAllScores()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    val scoresString = Transformations.map(scores) {
        formatScores(it, application.resources)
    }

    val clearButtonVisible = Transformations.map(scores) {
        it?.isNotEmpty()
    }

//    init {
//        loadScores()
//    }

//    private fun loadScores() {
//        uiScope.launch {
//            val scoresFromDB = getScoresFromDatabase()
//            scores = scoresFromDB ?: listOf<Score>()
//        }
//    }
//
//    private suspend fun getScoresFromDatabase(): List<Score>? {
//        return withContext(Dispatchers.IO) {
//            database.getAllScores()
//        }
//    }

    fun onClear() {
        uiScope.launch {
            clear()
            _showSnackbarEvent.value = true
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }
}