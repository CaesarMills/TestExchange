package com.example.testappexchange.core.di

import com.example.testappexchange.core.util.Constance.Companion.BASE_URL
import com.example.testappexchange.data.remote.ApiCurrency
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule{
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logInterceptor = HttpLoggingInterceptor()
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(logInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okhttp: OkHttpClient,
        gson: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okhttp)
            .addConverterFactory(gson)
            .build()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): ApiCurrency {
        return retrofit.create(ApiCurrency::class.java)
    }
}