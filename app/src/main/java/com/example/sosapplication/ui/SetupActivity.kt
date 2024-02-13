package com.example.sosapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sosapplication.MainActivity
import com.example.sosapplication.databinding.ActivitySetupBinding
import com.example.sosapplication.model.EmergencyNumber
import com.example.sosapplication.utils.SharedPreferencesHelper
import com.example.sosapplication.utils.Utility
import com.example.sosapplication.utils.Utility.startMoveUpAnimation
import pub.devrel.easypermissions.EasyPermissions

class SetupActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivitySetupBinding
    val userList: MutableList<EmergencyNumber> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!Utility.hasPermission(this)) {
            Utility.requestPermission(this)
        }

        with(binding) {
            val viewElements = listOf(userOne, phoneOne, phoneTwo, userTwo, proceedBtn)
            viewElements.forEachIndexed { index, view ->
                view.startMoveUpAnimation(this@SetupActivity)
            }

            proceedBtn.setOnClickListener {
                validateViews {
                    userList.add(
                        EmergencyNumber(
                            userOneText.text.toString(),
                            phoneOneText.text.toString()
                        )
                    )
                    userList.add(
                        EmergencyNumber(
                            userTwoText.text.toString(),
                            phoneTwoText.text.toString()
                        )
                    )
                    SharedPreferencesHelper.saveUserList(this@SetupActivity, userList)

                    val intent = Intent(this@SetupActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

        }


    }


    private fun validateViews(onClickListener: () -> Unit) {
        with(binding) {
            val isEmptyOrShort =
                { text: CharSequence? -> text.isNullOrBlank() || text.length <= 10 }

            if (userOneText.text.isNullOrBlank()) {
                userOneText.error = "Name One field can't be empty"
            } else if (isEmptyOrShort(phoneOneText.text)) {
                phoneOneText.error =
                    "Phone number One field can't be empty or less than 10 characters"
            } else if (userTwoText.text.isNullOrBlank()) {
                userTwoText.error = "Name Two field can't be empty"
            } else if (isEmptyOrShort(phoneTwoText.text)) {
                phoneTwoText.error =
                    "Phone number Two field can't be empty or less than 10 characters"
            } else {
                onClickListener.invoke()
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            // Permission is permanently denied, show rationale
            Utility.showDialog(
                this,
                "Please grant the camera and location permissions in settings"
            ) {
                finish()
            }
        } else {
            Utility.requestPermission(this)
        }
    }


}