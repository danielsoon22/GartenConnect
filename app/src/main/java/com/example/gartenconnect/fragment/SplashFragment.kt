package com.example.gartenconnect.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gartenconnect.R
import com.example.gartenconnect.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    private val splashTimeout: Long = 3000 // Tiempo de espera en milisegundos

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Iniciar la navegación a la siguiente pantalla después del tiempo de espera
        sharedPreferences =
            activity?.getSharedPreferences("GartenConnect", Context.MODE_PRIVATE) ?: return
        val isLoggedIn = sharedPreferences.getBoolean("LOGGED_IN", false)
        Handler().postDelayed({
            if (!isLoggedIn) {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
            } else {
                findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
            }
        }, splashTimeout)
        // Deshabilitar el botón de retroceso durante la pantalla de bienvenida
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.splashFragment, false)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
