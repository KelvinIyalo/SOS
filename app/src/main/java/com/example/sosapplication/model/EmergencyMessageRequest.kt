package com.example.sosapplication.model

data class EmergencyMessageRequest(
    val image: String,
    val location: UserLocation,
    val phoneNumbers: List<String>
)