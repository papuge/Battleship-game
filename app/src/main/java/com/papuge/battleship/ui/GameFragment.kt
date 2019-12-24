package com.papuge.battleship.ui


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var movesRef: DatabaseReference
    private lateinit var ansRef: DatabaseReference

    lateinit var moveNumText: TextView
    lateinit var myField: BattleField
    lateinit var userField: BattleField

    lateinit var opponentAnsPath: String
    lateinit var myAnsPath: String
    lateinit var opponentMovePath: String
    lateinit var myMovePath: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[GameViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        opponentAnsPath = if(viewModel.playerNum == 1) "p2a" else "p1a"
        myAnsPath = if(viewModel.playerNum == 1) "p1a" else "p2a"
        opponentMovePath = if(viewModel.playerNum == 1) "p2move" else "p1move"
        myMovePath = if(viewModel.playerNum == 1) "p1move" else "p2move"

        db = FirebaseDatabase.getInstance()
        gameRef = db.getReference("games/${viewModel.gameId}")
        movesRef = db.getReference("moves/${viewModel.gameId}")
        ansRef = db.getReference("answers/${viewModel.gameId}")

        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moveNumText = view.findViewById(R.id.tv_move_num)
        myField = view.findViewById(R.id.my_field)
        userField = view.findViewById(R.id.user_field)

        myField.shipRects = viewModel.shipRects
        myField.cells = viewModel.myGridCells
        userField.cells = viewModel.myMoveCells

        userField.setOnTouchListener { v, event ->
            when (event?.action) {
                MotionEvent.ACTION_UP -> {
                    val xTouch = event.x
                    val yTouch = event.y
                    val i = (xTouch / userField.cellWidth).toInt()
                    val j = (yTouch / userField.cellHeight).toInt()
                    if (viewModel.moveNum != viewModel.playerNum) {
                        touchHandler(i, j)
                    }
                }
            }
            v?.onTouchEvent(event) ?: true
        }

        subscribeOnOpponentMove()
        subscribeMoveChange()
    }

    private fun subscribeMoveChange() {
        gameRef.child("move").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val num = snapshot.getValue(Int::class.java) ?: return
                viewModel.moveNum = num
                moveNumText.text = getString(R.string.players_move, num.toString())
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }

    private fun subscribeOnOpponentMove() {
        movesRef.child(opponentMovePath).addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                var i = snapshot.child("first").getValue(Int::class.java)
                var j = snapshot.child("second").getValue(Int::class.java)

                if(i == null || j == null) {
                    i = 0
                    j = 0
                }

                if(i == -1 || j == -1) {
                    return
                }

                Log.d(TAG, "Init i j globals - $i, $j")

                val cell = viewModel.myGridCells[i][j]
                if(cell.state != CellState.EMPTY) {
                    ansRef.child(myAnsPath).setValue("not empty")
                }
                else if(cell.isShip) {
                    ansRef.child(myAnsPath).setValue("hit")
                    viewModel.myGridCells[i][j].state = CellState.HIT
                    myField.cells[i][j].state = CellState.HIT
                    myField.refreshCanvas()
                }
                else if(!cell.isShip) {
                    ansRef.child(myAnsPath).setValue("miss")
                    viewModel.myGridCells[i][j].state = CellState.MISS
                    myField.cells[i][j].state = CellState.MISS
                    myField.refreshCanvas()
                }
            }

            override fun onCancelled(p0: DatabaseError) {}

        })
    }

    private fun touchHandler(i: Int, j: Int) {
        makeNewMove(i, j)
        ansRef.child(opponentAnsPath).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(i == -1 || j == -1) {
                    return
                }
                Log.d(TAG, "Assign $i, $j")
                val status = snapshot.getValue(String::class.java)
                Log.d(TAG, "Status $status")
                when(status) {
                    "not empty" -> return
                    "hit" -> {
                        viewModel.myMoveCells[i][j].state = CellState.HIT
                        userField.cells[i][j].state = CellState.HIT
                        userField.refreshCanvas()
                        return
                    }
                    "kill" -> {}
                    "miss" -> {
                        viewModel.myMoveCells[i][j].state = CellState.MISS
                        val newMove = if(viewModel.playerNum == 1) 2 else 1
                        gameRef.child("move").setValue(newMove)
                        userField.cells[i][j].state = CellState.MISS
                        userField.refreshCanvas()
                        return
                    }
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
        subscribeMoveChange()
    }

    private fun makeNewMove(i: Int, j: Int) {
        movesRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                movesRef.child(myMovePath).child("first").setValue(i)
                movesRef.child(myMovePath).child("second").setValue(j)
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                b: Boolean,
                dataSnapshot: DataSnapshot?
            ) {}
        })
    }

    companion object {
        const val TAG = "GameFragment"
    }
}
