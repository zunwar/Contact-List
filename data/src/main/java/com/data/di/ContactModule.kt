package com.data.di

import com.data.InfoContactRepositoryImpl
import com.data.ListContactsRepositoryImpl
import com.domain.repository.InfoContactRepository
import com.domain.repository.ListContactsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ContactModule {

    @Binds
    @Singleton
    abstract fun bindListContactsRepository(impl: ListContactsRepositoryImpl): ListContactsRepository

    @Binds
    @Singleton
    abstract fun bindInfoContactRepository(impl: InfoContactRepositoryImpl): InfoContactRepository

}