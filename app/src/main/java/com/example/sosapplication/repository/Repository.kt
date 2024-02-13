package com.example.sosapplication.repository

import com.example.sosapplication.model.EmergencyMessageRequest
import com.example.sosapplication.model.EmergencyNetworkResponse
import com.example.sosapplication.service.EmergencyService
import com.example.sosapplication.utils.RepositoryResponse
import com.example.sosapplication.utils.Utility
import javax.inject.Inject

class Repository @Inject constructor(val apiService: EmergencyService) {

    suspend fun sendSOSMessage(emergencyMessageRequest: EmergencyMessageRequest): RepositoryResponse<EmergencyNetworkResponse> {

        return try {
            val result = apiService.sendOSOMessage(emergencyMessageRequest)
            Utility.getCallResponseState(result)
        } catch (exception: Exception) {
            return Utility.exceptionHandler(exception)
        }
    }
}