package com.papuge.battleship.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController

import com.papuge.battleship.R


class CreateGameFragment : Fragment() {

    private lateinit var startGame: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startGame = view.findViewById(R.id.btn_start_game)
        startGame.setOnClickListener {
            val direction = CreateGameFragmentDirections
                .actionCreateGameFragmentToAllocateFragment()
            findNavController().navigate(direction)
        }
    }

}
