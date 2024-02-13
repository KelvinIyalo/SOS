package com.example.sosapplication.di


import com.example.merchantarounddemo.provider.AppConfig
import com.example.sosapplication.provider.AppConfigImpl
import com.example.sosapplication.service.EmergencyService
import com.example.sosapplication.utils.Utility
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppConfig() = AppConfigImpl() as AppConfig

    @Singleton
    @Provides
    fun provideCarsService(appConfig: AppConfig): EmergencyService {
        val baseUrl = Utility.BASE_URL
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(appConfig.okHttpClient)
            .build()
            .create(EmergencyService::class.java)
    }

}