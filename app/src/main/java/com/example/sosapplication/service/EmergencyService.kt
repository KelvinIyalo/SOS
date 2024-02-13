package com.example.sosapplication.service

import com.example.sosapplication.model.EmergencyMessageRequest
import com.example.sosapplication.model.EmergencyNetworkResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EmergencyService {

    @POST("v1/create")
    suspend fun sendOSOMessage(
        @Body body : EmergencyMessageRequest
    ): Response<EmergencyNetworkResponse>
}