package com.example.oldlady.game

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.oldlady.R
import com.example.oldlady.databinding.FragmentGameBinding

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding
    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<FragmentGameBinding>(
            inflater, R.layout.fragment_game,container, false
        )

        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)
        binding.gameViewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.startGame()

        setHasOptionsMenu(true)
        setListeners()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun setListeners() {
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
        when (view.id) {
            R.id.pos_0_0_text -> viewModel.executePlay(0, 0)
            R.id.pos_0_1_text -> viewModel.executePlay(0, 1)
            R.id.pos_0_2_text -> viewModel.executePlay(0, 2)
            R.id.pos_1_0_text -> viewModel.executePlay(1, 0)
            R.id.pos_1_1_text -> viewModel.executePlay(1, 1)
            R.id.pos_1_2_text -> viewModel.executePlay(1, 2)
            R.id.pos_2_0_text -> viewModel.executePlay(2, 0)
            R.id.pos_2_1_text -> viewModel.executePlay(2, 1)
            R.id.pos_2_2_text -> viewModel.executePlay(2, 2)
        }
    }

    private fun handleGameReset() {
        viewModel.resetGame()
    }

    private fun handleGameContinue() {
        viewModel.continueGame()
    }
}