package com.mihir.daggerhiltmvvmkotlin.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.mihir.daggerhiltmvvmkotlin.data.local.dao.UserDao
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.di.module.CompositeDisposableRx2
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserDataSourceFactory @Inject constructor(
    private val dataSource: UserKeysDataSource
):DataSource.Factory<String,UserEntity>() {

    private val userLiveDataSource= MutableLiveData<UserKeysDataSource>()
    override fun create(): DataSource<String, UserEntity> {
        userLiveDataSource.postValue(dataSource)
        return dataSource
    }
}