package com.example.sosapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.sosapplication.R
import com.example.sosapplication.adapter.RegisteredNumberAdapter
import com.example.sosapplication.databinding.FragmentSettingsBinding
import com.example.sosapplication.model.EmergencyNumber
import com.example.sosapplication.utils.SharedPreferencesHelper
import com.example.sosapplication.utils.Utility


class SettingsFragment : Fragment(R.layout.fragment_settings) {
    lateinit var binding: FragmentSettingsBinding
    private lateinit var registerNumberAdapter: RegisteredNumberAdapter
    val userList: MutableList<EmergencyNumber> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSettingsBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        initTransactionsRecyclerViewAdapter()
        binding.addBtn.setOnClickListener {
            Utility.showUpdateDialog(requireActivity()) { dialogView, dialog ->
                with(dialogView) {
                    val isButtonEnabled =
                        phoneOneText.text?.length!! <= 10 || userOneText.text!!.isEmpty()
                    proceedBtn.apply {
                        isEnabled = isButtonEnabled
                        setOnClickListener {
                            addNumber(userOneText.text.toString(), phoneOneText.text.toString())
                            dialog.dismiss()
                        }
                    }
                    cancelButton.setOnClickListener {
                        dialog.dismiss()
                    }


                }
            }
        }
    }

    private fun addNumber(name: String, phone: String) {
        val existingUsers = SharedPreferencesHelper.getUserList(requireContext())
        val newUser = EmergencyNumber(
            name = name,
            phoneNumber = phone
        )
        userList.add(newUser)
        existingUsers.forEach {
            userList.add(it)
        }
        SharedPreferencesHelper.saveUserList(requireContext(), userList)
    }

    private fun initTransactionsRecyclerViewAdapter() {
        registerNumberAdapter =
            RegisteredNumberAdapter { position: Int, itemAtPosition: EmergencyNumber ->


            }
        binding.recyclerView.adapter = registerNumberAdapter
        registerNumberAdapter.submitList(SharedPreferencesHelper.getUserList(requireContext()))
    }


}