package com.example.sosapplication.ui

import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.sosapplication.R
import com.example.sosapplication.databinding.FragmentCameraBinding
import com.example.sosapplication.model.EmergencyMessageRequest
import com.example.sosapplication.model.UiState
import com.example.sosapplication.model.UserLocation
import com.example.sosapplication.utils.Utility
import com.example.sosapplication.viewmodel.EmergencyViewModel
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import dagger.hilt.android.AndroidEntryPoint
import java.util.Base64

@AndroidEntryPoint
class CameraFragment : Fragment(R.layout.fragment_camera) {
    lateinit var binding: FragmentCameraBinding
    private val viewModel: EmergencyViewModel by activityViewModels()
    lateinit var camera: CameraView
    var encodedString: String? = null
    lateinit var dialog: AlertDialog
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCameraBinding.bind(view)
        with(binding) {
            camera = cameraView.apply {
                setLifecycleOwner(this@CameraFragment)
                addCameraListener(object : CameraListener() {
                    @RequiresApi(Build.VERSION_CODES.O)
                    override fun onPictureTaken(result: PictureResult) {
                        encodedString = Base64.getEncoder().encodeToString(result.data)
                        this@apply.destroy()
                        sendSOSMessage(encodedString.toString())
                    }
                })
            }
            floatingActionButton.setOnClickListener {
                camera.takePicture()
            }
        }

    }

    private fun sendSOSMessage(image: String) {

        viewModel.sendSOSMessage(image, requireContext())
            .observe(viewLifecycleOwner, Observer { response ->

                when (response) {

                    is UiState.Loading -> {
                        Log.d("XXX Loading","Loading")
                        dialog = Utility.showLoadingDialog(requireActivity())
                    }

                    is UiState.Success -> {
                        dialog.dismiss()
                        Log.d("XXX Success","Success")
                        Utility.showDialog(requireActivity(), response.data.message) {
                            findNavController().popBackStack()
                        }
                    }

                    is UiState.DisplayError -> {
                        dialog.dismiss()
                        Log.d("XXX DisplayError","DisplayError")
                        Utility.showMessage(response.error, binding.root)
                    }

                    is UiState.ApiError -> {
                        dialog.dismiss()
                        Utility.showMessage(response.error, binding.root)
                    }

                }

            })

    }

    override fun onPause() {
        super.onPause()
        camera.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.destroy()
    }
}