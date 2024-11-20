package com.example.testappexchange.core.di

import android.content.Context
import androidx.room.Room
import com.example.testappexchange.core.util.Constance.Companion.DATABASE_CURRENCY_NAME
import com.example.testappexchange.data.local.LocalDataSource
import com.example.testappexchange.data.local.UserRepositoryImpl
import com.example.testappexchange.data.local.database.UserAccountDao
import com.example.testappexchange.data.local.database.UserDataBase
import com.example.testappexchange.data.local.database.UserProfileDao
import com.example.testappexchange.data.remote.RemoteDataSource
import com.example.testappexchange.data.remote.RepositoryImpl
import com.example.testappexchange.domain.repositories.Repository
import com.example.testappexchange.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule{

    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context = context,
        UserDataBase::class.java,
        DATABASE_CURRENCY_NAME,
    ).fallbackToDestructiveMigration()
        .build()


    @Singleton
    @Provides
    fun provideUserProfileDao(userDatabase: UserDataBase): UserProfileDao = userDatabase.userProfileDao()


    @Singleton
    @Provides
    fun provideUserAccountDao(userDatabase: UserDataBase): UserAccountDao = userDatabase.userAccountDao()

    @Provides
    @Singleton
    fun provideRepository(
        remoteDataSource: RemoteDataSource
    ): Repository {
        return RepositoryImpl(remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        localDataSource: LocalDataSource
    ): UserRepository {
        return UserRepositoryImpl(localDataSource)
    }
}