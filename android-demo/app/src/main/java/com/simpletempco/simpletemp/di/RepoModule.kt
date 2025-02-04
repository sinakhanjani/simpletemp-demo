package com.simpletempco.simpletemp.di

import android.app.Application
import androidx.preference.PreferenceManager
import com.simpletempco.simpletemp.data.AppRepository
import com.simpletempco.simpletemp.data.local.LocalRepo
import com.simpletempco.simpletemp.data.local.preference.AppPreference
import com.simpletempco.simpletemp.data.remote.RemoteRepo
import com.simpletempco.simpletemp.data.remote.SimpleTempApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {

    @Provides
    @Singleton
    fun provideAppPreference(application: Application) =
        AppPreference(PreferenceManager.getDefaultSharedPreferences(application))

    @Provides
    @Singleton
    fun provideLocalRepo(appPreference: AppPreference) = LocalRepo(appPreference)

    @Provides
    @Singleton
    fun provideRemoteRepo(simpleTempApi: SimpleTempApi): RemoteRepo {
        return RemoteRepo(simpleTempApi)
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        remoteRepo: RemoteRepo,
        localRepo: LocalRepo
    ): AppRepository {
        return AppRepository(
            remoteRepo,
            localRepo
        )
    }

}