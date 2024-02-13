package com.example.merchantarounddemo.provider

import okhttp3.OkHttpClient

interface AppConfig {

    /**
     * Configuration, such as base urls, api keys, etc. Each module can use this to access the configs it care about
     */
    val configMap: Map<String, String>

    /**
     * Okhttp client should be shared across the app for performance reasons. Other modules can have access to the okhttp client.
     * They can however customize it by creating newBuilder out of the shared client. Like so
     *
     * kotlin
     *  okHttpClient.newBuilder()
     *      .readTimeout(500, TimeUnit.MILLISECONDS)
     *      .build()
     *
     */
    val okHttpClient: OkHttpClient

}