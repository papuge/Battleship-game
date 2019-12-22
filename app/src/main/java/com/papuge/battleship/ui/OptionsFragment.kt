package com.papuge.battleship.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth

import com.papuge.battleship.R

class OptionsFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var signOutTv: TextView
    private lateinit var startNewGame: Button
    private lateinit var joinGame: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(context!!, gso)

        auth = FirebaseAuth.getInstance()
        return inflater.inflate(R.layout.fragment_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        signOutTv = view.findViewById(R.id.tv_sign_out)
        signOutTv.setOnClickListener {
            signOut()
        }

        startNewGame = view.findViewById(R.id.btn_start_new_game)
        startNewGame.setOnClickListener {
            val direction = OptionsFragmentDirections
                .actionOptionsFragmentToCreateGameFragment()
            findNavController().navigate(direction)
        }

        joinGame = view.findViewById(R.id.btn_join_game)
        joinGame.setOnClickListener {
            val direction = OptionsFragmentDirections
                .actionOptionsFragmentToJoinGameFragment()
            findNavController().navigate(direction)
        }
    }


    private fun signOut() {
        auth.signOut()

        googleSignInClient.signOut().addOnCompleteListener {
            val direction = OptionsFragmentDirections
                .actionOptionsFragmentToAuthFragment2()
            findNavController().navigate(direction)
        }
    }

}
