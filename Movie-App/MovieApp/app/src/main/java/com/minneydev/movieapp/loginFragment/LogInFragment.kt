package com.minneydev.movieapp.loginFragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.minneydev.movieapp.App
import com.minneydev.movieapp.R
import com.minneydev.movieapp.data.User
import kotlinx.android.synthetic.main.fragment_log_in.*
import kotlin.system.exitProcess

class  LogInFragment : Fragment() {

//    private val userRepository by lazy { UserRepository(App.userDatabase) }
//    private val currentUser by lazy {lifecycle.coroutineScope.launch {
//        userRepository.getLoggedInUser(true)
//    }}

    private val currentUser: User? = App.userDataManager.getLoggedInUser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                closeApp()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_log_in, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (currentUser != null) { goToMainScreen() }

        loginBtn.setOnClickListener {
//            goToTestFragment()
            validateLogin()
        }

        registerTextView.setOnClickListener {
            goToRegisterScreen()
        }

    }

    private fun validateLogin() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val tempUser = App.userDataManager.fetchUser()

        if ((tempUser != null) && (email == tempUser.email) && (password == tempUser.password)) {
            pass(tempUser)
        }else {
            fail()
        }
    }

    private fun pass(user: User) {
        user.isLoggedIn = true
        App.userDataManager.logInUser()
        goToMainScreen()

    }

    private fun fail() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(R.string.failed_login_title)
                .setMessage(R.string.failed_login_message)
                .show()
        }
        emailEditText.text.clear()
        passwordEditText.text.clear()
    }

    private fun goToMainScreen() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_logInFragment_to_movieFragment)
        }
    }

    private fun goToRegisterScreen() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_logInFragment_to_registerFragment)
        }
    }

    private fun goToTestFragment() {
        view?.let {
            Navigation.findNavController(it).navigate(R.id.action_logInFragment_to_testFragment)
        }
    }

    private fun closeApp() {
        exitProcess(0)
    }

}