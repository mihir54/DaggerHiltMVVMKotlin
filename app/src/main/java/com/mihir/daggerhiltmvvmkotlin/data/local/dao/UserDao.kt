package com.mihir.daggerhiltmvvmkotlin.data.local.dao

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.rxjava3.core.Observable


@Dao
interface UserDao {

    @Insert(onConflict = REPLACE)
    fun insert(userEntity: UserEntity)

    @Query("SELECT * FROM user")
    fun getAllUsers() : Flowable<List<UserEntity>>

    @Query("SELECT count(*) from user")
    fun count(): Flowable<Int>

    //searching
    @Query("SELECT * FROM user where name LIKE :query || '%'")
    fun searchUsers(query: String?): Single<List<UserEntity>>

    @Query("SELECT * FROM user ORDER BY id ASC limit :requestedLoadSize")
    fun getAllUsersById(requestedLoadSize: Int): Single<List<UserEntity>>

    //searching
    @Query("SELECT * FROM user where name LIKE :query || '%'")
    fun searchPagedUsers(query: String?): DataSource.Factory<Int,UserEntity>

    //paging
    @Query("SELECT * FROM user ORDER BY id ASC")
    fun allUsers(): DataSource.Factory<Int,UserEntity>

    @Query("SELECT * FROM user ORDER BY name ASC limit :requestedLoadSize")
    fun pagedUsers(requestedLoadSize : Int) : List<UserEntity>

    @Query("SELECT * FROM user WHERE name > :key ORDER BY name ASC limit :requestedLoadSize")
    fun pagedUSerAfter(key: String, requestedLoadSize : Int) : List<UserEntity>

}