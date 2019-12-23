package com.papuge.battleship.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.database.*
import com.papuge.battleship.BattleField

import com.papuge.battleship.R
import com.papuge.battleship.game.CellState
import com.papuge.battleship.viewModels.GameViewModel


class GameFragment : Fragment() {

    lateinit var viewModel: GameViewModel

    private lateinit var db: FirebaseDatabase
    private lateinit var gameRef: DatabaseReference

    lateinit var moveNumText: TextView
    lateinit var myField: BattleField
    lateinit var userField: BattleField

    lateinit var opponentAnsPath: String
    lateinit var opponentMovePath: String
    lateinit var myMovePath: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[GameViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        opponentAnsPath = if(viewModel.playerNum == 1) "p2answer" else "p1answer"
        opponentMovePath = if(viewModel.playerNum == 1) "p2move" else "p1move"
        myMovePath = if(viewModel.playerNum == 1) "p1move" else "p2move"

        db = FirebaseDatabase.getInstance()
        gameRef = db.getReference("games/${viewModel.gameId}")

        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveNumText = view.findViewById(R.id.tv_move_num)
        myField = view.findViewById(R.id.my_field)
        userField = view.findViewById(R.id.user_field)

        myField.shipRects = viewModel.shipRects

    }

    private fun subscribeMoveChange() {
        gameRef.child("move").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val num = snapshot.getValue(Int::class.java)!!
                viewModel.moveNum = num
                moveNumText.text = getString(R.string.players_move, num.toString())
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }

    private fun touchHandler(i: Int, j: Int) {
        if(viewModel.moveNum != viewModel.playerNum) {
            return
        }

        val cell = viewModel.myGridCells[i][j]
        if(cell.state != CellState.EMPTY) {
            return
        }
        if(!cell.isShip) {
            cell.state = CellState.MISS
            val newMove = if(viewModel.playerNum == 1) 2 else 1
            gameRef.child("move").setValue(newMove)
        }
    }
}
