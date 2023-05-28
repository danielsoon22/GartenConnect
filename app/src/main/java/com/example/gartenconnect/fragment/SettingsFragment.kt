package com.example.gartenconnect.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.gartenconnect.R
import com.example.gartenconnect.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsFragment : PreferenceFragmentCompat() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var sharePreference: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preferences, rootKey)
        sharePreference = requireContext().getSharedPreferences("GartenConnect", Context.MODE_PRIVATE)
        findPreference<Preference>("LOGGED_IN")?.setOnPreferenceClickListener {
            logout()
            true
        }

        findPreference<EditTextPreference>("username")?.text = sharePreference.getString("username", "")
        findPreference<EditTextPreference>("email")?.text = sharePreference.getString("email", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        // Obtener referencia al DrawerLayout desde el NavController
        drawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        // Desactivar el menú del drawer en el fragmento de configuración
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onPause() {
        super.onPause()
        // Habilitar el menú del drawer cuando el fragmento de configuración ya no esté visible
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun logout() {
        val sharePreferences =
            requireActivity().getSharedPreferences("GartenConnect", android.content.Context.MODE_PRIVATE)
        val editor = sharePreferences.edit()
        editor.putBoolean("LOGGED_IN", false)
        editor.apply()
        FirebaseAuth.getInstance().signOut()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.settingsFragment, false)
            }
        })
        findNavController().navigate(
            R.id.loginFragment, null,
            NavOptions.Builder().setPopUpTo(R.id.homeFragment, true).build()
        )
        if (requireActivity() is MainActivity) {
            val mainActivity = requireActivity() as MainActivity
            mainActivity.setLoggedInState(false)
        }
    }
}



