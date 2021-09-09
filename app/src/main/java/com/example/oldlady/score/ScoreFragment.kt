package com.example.oldlady.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.oldlady.R
import com.example.oldlady.databinding.FragmentScoreBinding
import com.google.android.material.snackbar.Snackbar

class ScoreFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentScoreBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_score, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = ScoreDatabase.getInstance(application).scoreDatabaseDao
        val viewModelFactory = ScoreViewModelFactory(dataSource, application)
        val scoreViewModel = ViewModelProvider(this, viewModelFactory).get(ScoreViewModel::class.java)

        binding.scoreViewModel = scoreViewModel
        binding.lifecycleOwner = this

        val adapter = ScoreAdapter()
        binding.scoreList.adapter = adapter
        scoreViewModel.scores.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        scoreViewModel.showSnackBarEvent.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.cleared_message),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                scoreViewModel.doneShowingSnackbar()
            }
        })

        return binding.root
    }

}