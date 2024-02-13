package com.example.sosapplication.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.sosapplication.model.EmergencyMessageRequest
import com.example.sosapplication.model.UiState
import com.example.sosapplication.model.UserLocation
import com.example.sosapplication.repository.Repository
import com.example.sosapplication.utils.RepositoryResponse
import com.example.sosapplication.utils.SharedPreferencesHelper
import com.example.sosapplication.utils.Utility
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmergencyViewModel @Inject constructor(val repo: Repository) : ViewModel() {

    fun sendSOSMessage(encodedImage: String, context: Context) = liveData {

        emit(UiState.Loading)
        val emergencyMessage = EmergencyMessageRequest(
            image = encodedImage,
            location = UserLocation(
                Utility.currentLocation?.latitude.toString(),
                Utility.currentLocation?.longitude.toString()
            ),
            phoneNumbers = SharedPreferencesHelper.getUserList(context).map { it.phoneNumber }
        )
        when (val result = repo.sendSOSMessage(emergencyMessage)) {
            is RepositoryResponse.Success -> {
                emit(UiState.Success(result.data))
            }

            is RepositoryResponse.Error -> {
                emit(UiState.DisplayError(result.error))
            }

            is RepositoryResponse.ApiError -> {
                emit(UiState.ApiError(result.apiError))
            }
        }

    }
}