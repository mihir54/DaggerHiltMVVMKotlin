package com.mihir.daggerhiltmvvmkotlin.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mihir.daggerhiltmvvmkotlin.data.local.DatabaseService
import com.mihir.daggerhiltmvvmkotlin.data.local.dao.UserDao
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.data.remote.ApiHelper
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.city.CITY
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.city.CityResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.DISTRICT
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.DistrictResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.state.Data
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.state.STATE
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.state.StateResponse
import com.mihir.daggerhiltmvvmkotlin.di.module.CompositeDisposableRx2
import com.mihir.daggerhiltmvvmkotlin.utils.Constants
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxjava3.core.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiHelper: ApiHelper,
    private val databaseService: DatabaseService,
    userDao: UserDao,
    @CompositeDisposableRx2 compositeDisposableRx2: CompositeDisposable
) {

    /*fun getCityData() : Single<CityResponse> = apiHelper.getCities()

    fun getStateData() : Single<StateResponse> = apiHelper.getStates()

    fun getDistrictData() : Single<DistrictResponse> = apiHelper.getDistricts()*/



    var uDao = userDao
    var comp = compositeDisposableRx2


    fun insertUser(userEntity: UserEntity) =
        comp.add(Observable.fromCallable { uDao.insert(userEntity) }
            .subscribeOn(Schedulers.io())
            .subscribe())

    fun getCount() = databaseService.userDao().count()

    fun getAllUser() = LiveDataReactiveStreams.fromPublisher(
        databaseService.userDao().getAllUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
    )

    fun searchUsers(it: String): io.reactivex.Single<List<UserEntity>> = uDao.searchUsers(it)
    fun searchPagedUsers(it: String): DataSource.Factory<Int,UserEntity> = uDao.searchPagedUsers(it)

    fun allUSers() : DataSource.Factory<Int,UserEntity> = uDao.allUsers()

    fun allCustomPagedUsers(requestedLoadSize : Int) : List<UserEntity> =uDao.pagedUsers(requestedLoadSize);

    fun allCustomPagedAfterUsers(key : String,requestedLoadSize : Int) : List<UserEntity> =uDao.pagedUSerAfter(key,requestedLoadSize);

    /*fun fetchUserPagedList (compositeDisposable: CompositeDisposable) :LiveData<PagedList<UserEntity>>{
        userDataSourceFactory= UserDataSourceFactory(uDao,compositeDisposable)

        val config =PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(Constants.POST_PR_PAGE)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory,config).build()
        return userPagedList
    }*/

    fun getStateData(): Single<StateResponse> {
        val listState = mutableListOf<STATE>()
        for (s in 1..4) {
            listState.add(
                STATE(
                    CountryID = s,
                    StateCode = "sc $s",
                    StateID = s,
                    StateName = "stateName $s",
                    StateType = "S"
                )
            )
        }

        val data = Data(STATE = listState)

        return Single.create {
            val stateResponse = StateResponse(Data = data, Message = "Success", MsgCode = 1)
            it.onSuccess(stateResponse)
        }
    }

    fun getDistrictData(): Single<DistrictResponse> {
        val listDistrict = mutableListOf<DISTRICT>()
        for (d in 1..3)
            listDistrict.add(
                DISTRICT(
                    DistrictID = d * 1,
                    DistrictName = "district $d",
                    StateID = d
                )
            )

        for (d in 2..5)
            listDistrict.add(
                DISTRICT(
                    DistrictID = d * 2,
                    DistrictName = "district $d",
                    StateID = d
                )
            )

        for (d in 3..8) {
            listDistrict.add(
                DISTRICT(
                    DistrictID = d * 3,
                    DistrictName = "district $d",
                    StateID = d
                )
            )
        }

        val data =
            com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.Data(DISTRICT = listDistrict)
        return Single.create {
            it.onSuccess(DistrictResponse(Data = data, Message = "Success", MsgCode = 1))
        }
    }

    fun getCityData(): Single<CityResponse> {
        val listCity = mutableListOf<CITY>()
        for (c in 1..3)
            listCity.add(
                CITY(
                    CityGrade = 1,
                    CityID = c,
                    CityName = "city $c",
                    DistrictID = c * 1
                )
            )

        for (c in 2..5)
            listCity.add(
                CITY(
                    CityGrade = 1,
                    CityID = c,
                    CityName = "city $c",
                    DistrictID = c * 2
                )
            )

        for (c in 3..8) {
            listCity.add(
                CITY(
                    CityGrade = 1,
                    CityID = c,
                    CityName = "city $c",
                    DistrictID = c * 3
                )
            )
        }

        val data =
            com.mihir.daggerhiltmvvmkotlin.data.remote.response.city.Data(CITY = listCity)
        return Single.create {
            it.onSuccess(CityResponse(Data = data, Message = "Success", MsgCode = 1))
        }
    }



}


