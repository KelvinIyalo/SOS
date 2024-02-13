package com.example.sosapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import com.example.sosapplication.MainActivity
import com.example.sosapplication.R
import com.example.sosapplication.databinding.FragmentHomeBinding
import com.example.sosapplication.utils.SharedPreferencesHelper
import com.example.sosapplication.utils.Utility
import com.example.sosapplication.utils.Utility.startBounceAnimation
import com.example.sosapplication.utils.Utility.startRotateAnimation
import pub.devrel.easypermissions.EasyPermissions

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.emergencyBtn.setOnClickListener {
            binding.emergencyBtn.startBounceAnimation(requireContext())
            Handler().postDelayed({
                if (Utility.isLocationAvailable == true) {
                 findNavController().navigate(R.id.action_navigation_home_to_cameraFragment)
                } else {
                    Utility.showMessage(
                        "Device location not available, Check Device Internet and GPS",
                        binding.root
                    )
                }
            }, 700)
        }

        binding.settingBtn.setOnClickListener {
            binding.settingBtn.startRotateAnimation(requireContext())
            Handler().postDelayed({
                findNavController().navigate(R.id.action_navigation_home_to_settingsFragment)
            }, 700)
        }
    }



}