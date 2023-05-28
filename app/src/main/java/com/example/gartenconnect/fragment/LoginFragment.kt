package com.example.gartenconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.gartenconnect.R
import com.example.gartenconnect.ViewModel.LoginViewModel
import com.example.gartenconnect.ViewModel.LoginViewModelFactory
import com.example.gartenconnect.activity.MainActivity
import com.example.gartenconnect.databinding.FragmentLoginBinding
import com.example.gartenconnect.model.AuthState
import com.example.gartenconnect.model.ParentRepository

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).supportActionBar?.hide()
        val repository = ParentRepository()
        viewModel = ViewModelProvider(this, LoginViewModelFactory(repository)).get(LoginViewModel::class.java)

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password, requireContext())
        }

        viewModel.authState.observe(viewLifecycleOwner) { authState ->
            when (authState) {
                AuthState.SUCCESS -> {
                    viewModel.saveLoginStatus(requireContext(), true)
                    navigateToHomeFragment()
                }
                AuthState.ERROR -> displayLoginFailedToast()
            }
        }

        // Deshabilitar el botón de retroceso durante la pantalla de inicio de sesión
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.loginFragment, false)
            }
        })

        // Obtener referencia al DrawerLayout desde el NavController
        //drawerLayout = requireActivity().findViewById(R.id.drawer_layout)

        // Desactivar el menú del drawer en la pantalla de inicio de sesión
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToHomeFragment() {
        findNavController().navigate(
            R.id.action_loginFragment_to_homeFragment, null,
            NavOptions.Builder().setPopUpTo(R.id.loginFragment, true).build()
        )
    }

    private fun displayLoginFailedToast() {
        Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        // Habilitar el menú del drawer en el HomeFragment
        //drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }
}

