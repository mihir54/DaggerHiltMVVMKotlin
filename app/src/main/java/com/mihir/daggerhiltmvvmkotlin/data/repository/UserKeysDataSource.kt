package com.mihir.daggerhiltmvvmkotlin.data.repository

import android.util.Log
import androidx.paging.ItemKeyedDataSource
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import javax.inject.Inject

class UserKeysDataSource @Inject constructor(private val mainRepository: MainRepository) : ItemKeyedDataSource<String, UserEntity>() {

    companion object{
        private val TAG ="UserKeysDataSource"
    }

    override fun loadInitial(
        params: LoadInitialParams<String>,
        callback: LoadInitialCallback<UserEntity>
    ) {
        Log.d(TAG,"loadInitial : ${params.requestedLoadSize}")
        val items= mainRepository.allCustomPagedUsers(params.requestedLoadSize)
        callback.onResult(items)
    }

    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<UserEntity>) {
        Log.d(TAG,"loadAfter : ${params.requestedLoadSize}")
        val items= mainRepository.allCustomPagedAfterUsers(params.key, params.requestedLoadSize)
        callback.onResult(items)
    }

    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<UserEntity>) {

    }

    override fun getKey(item: UserEntity): String = item.name.toString()
}