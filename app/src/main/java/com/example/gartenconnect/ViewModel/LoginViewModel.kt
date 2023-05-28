package com.example.gartenconnect.ViewModel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.AuthState
import com.example.gartenconnect.model.ParentRepository
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel(private val repository : ParentRepository) : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val TAG = "Log:LoginVM"
    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    fun login(email: String, password: String, context: Context) {
        repository.login(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                _authState.value = AuthState.SUCCESS
                saveLoginStatus(context, true)
            } else {
                _authState.value = AuthState.ERROR
            }
        }
    }

    fun saveLoginStatus(context: Context, isLoggedIn: Boolean) {
        val sharedPref = context.getSharedPreferences("GartenConnect", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("LOGGED_IN", isLoggedIn)
            apply()
        }
    }
}
