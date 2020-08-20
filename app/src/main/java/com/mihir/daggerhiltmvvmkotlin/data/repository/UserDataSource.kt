package com.mihir.daggerhiltmvvmkotlin.data.repository

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.mihir.daggerhiltmvvmkotlin.data.local.dao.UserDao
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.di.module.CompositeDisposableRx2
import com.mihir.daggerhiltmvvmkotlin.utils.Constants
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.disposables.ArrayCompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UserDataSource constructor(userDao: UserDao, compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, UserEntity>() {

    private var page= Constants.FIRST_PAGE
    private var comp=compositeDisposable
    private var uDao= userDao

    companion object{
        const val TAG="UserDataSource";
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, UserEntity>
    ) {
        comp.add(
            uDao.getAllUsersById(params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it,null,page+1)
                },{
                    Log.e(TAG,it.message.toString())
                })

        )
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, UserEntity>) {
        comp.add(
            uDao.getAllUsersById(params.requestedLoadSize)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    callback.onResult(it,params.key+1)
                },{
                    Log.e(TAG,it.message.toString())
                })

        )
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, UserEntity>) {

    }
}