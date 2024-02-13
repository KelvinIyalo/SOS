package com.example.sosapplication.provider

import com.example.merchantarounddemo.provider.AppConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class AppConfigImpl : AppConfig {
    override val configMap: Map<String, String>
        get() = TODO()


    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override val okHttpClient = OkHttpClient.Builder().apply {
        writeTimeout(60, TimeUnit.SECONDS)
        readTimeout(60, TimeUnit.SECONDS)
        addInterceptor(loggingInterceptor)
    }.build()
}