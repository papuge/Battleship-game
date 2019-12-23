package com.papuge.battleship.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.papuge.battleship.BattleField

import com.papuge.battleship.R
import com.papuge.battleship.viewModels.GameViewModel


class GameFragment : Fragment() {

    lateinit var viewModel: GameViewModel

    lateinit var moveNumText: TextView
    lateinit var myField: BattleField
    lateinit var userField: BattleField

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[GameViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myField = view.findViewById(R.id.my_field)
        myField.shipRects = viewModel.shipRects

    }

}
