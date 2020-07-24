package com.example.wallpaper

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup navController
        navController = Navigation.findNavController(view)

        // Check if user already login
        if (firebaseAuth.currentUser == null){
            // Not login, create new account
            register_text.text = "Creating new account"
            firebaseAuth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful){
                    // Account created, go to home page
                    register_text.text = "Account created, Logging in"
                    navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
                }
                else{
                    // Error
                    register_text.text = "Error: ${it.exception!!.message}"
                }
            }
        }
        else{
            // Login, send back to homepage
            navController!!.navigate(R.id.action_registerFragment_to_homeFragment)
        }
    }

}
