package com.data.di

import com.data.remote.network.ContactApiService
import com.data.remote.network.NetworkResultCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

object NetworkModule {
    private const val BASE_URL =
        "https://raw.githubusercontent.com/SkbkonturMobile/mobile-test-droid/master/json/"

    @Module
    @InstallIn(SingletonComponent::class)
    object NetworkModule {

        @Provides
        @Singleton
        fun provideMoshi(): Moshi {
            return Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()
        }

        @Provides
        @Singleton
        fun provideContactsApi(moshi: Moshi): ContactApiService {
            return Retrofit.Builder()
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(NetworkResultCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build()
                .create(ContactApiService::class.java)
        }
    }


}