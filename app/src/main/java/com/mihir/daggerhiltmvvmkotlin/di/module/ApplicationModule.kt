package com.mihir.daggerhiltmvvmkotlin.di.module

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mihir.daggerhiltmvvmkotlin.BuildConfig
import com.mihir.daggerhiltmvvmkotlin.data.local.DatabaseService
import com.mihir.daggerhiltmvvmkotlin.data.local.MIGRATION_8_9
import com.mihir.daggerhiltmvvmkotlin.data.remote.ApiHelper
import com.mihir.daggerhiltmvvmkotlin.data.remote.ApiHelperImpl
import com.mihir.daggerhiltmvvmkotlin.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.disposables.CompositeDisposable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule() {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideAPIService(retrofit: Retrofit): ApiService? = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Provides
    fun provideDatabaseService(@ApplicationContext context: Context): DatabaseService =
        Room.databaseBuilder(
            context,
            DatabaseService::class.java,
            "user.db"
        )
            .fallbackToDestructiveMigration()
            .addMigrations(MIGRATION_8_9)
            .build()
    @Provides
    fun provideUserDao(databaseService: DatabaseService)=databaseService.userDao()


    @Provides
    @CompositeDisposableRx3
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @CompositeDisposableRx2
    fun provideCompositeDisposableRx2() = io.reactivex.disposables.CompositeDisposable()


}