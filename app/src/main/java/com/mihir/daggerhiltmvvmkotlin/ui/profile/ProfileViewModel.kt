package com.mihir.daggerhiltmvvmkotlin.ui.profile

import android.util.Log
import android.widget.EditText
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.jakewharton.rxbinding2.widget.RxTextView
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent
import com.mihir.daggerhiltmvvmkotlin.data.local.entity.UserEntity
import com.mihir.daggerhiltmvvmkotlin.data.repository.MainRepository
import com.mihir.daggerhiltmvvmkotlin.data.repository.UserDataSourceFactory
import com.mihir.daggerhiltmvvmkotlin.di.module.CompositeDisposableRx2
import com.mihir.daggerhiltmvvmkotlin.di.module.CompositeDisposableRx3
import com.mihir.daggerhiltmvvmkotlin.ui.adapter.UserAdapter
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import kotlin.collections.set


class ProfileViewModel @ViewModelInject constructor(

    private val mainRepository: MainRepository,
    @CompositeDisposableRx3 private val compositeDisposable: CompositeDisposable,
    @CompositeDisposableRx2 private val compositeDisposableRx2: io.reactivex.disposables.CompositeDisposable,
    dataSourceFactory: UserDataSourceFactory
): ViewModel(){

    companion object {
        const val TAG="ProfileViewModel"
        private const val PAGE_SIZE=3
        private const val INITIAL_LOAD_SIZE=10
    }


    private var edtSearch: EditText? = null
    private lateinit var rvAdapter: UserAdapter
    var stateListLiveData = MutableLiveData<List<String>>()
    var stateMapLiveData = MutableLiveData<HashMap<String,Int>>()
    var districtListLiveData = MutableLiveData<List<String>>()
    var districtMapLiveData = MutableLiveData<HashMap<String,Int>>()
    var cityListLiveData = MutableLiveData<List<String>>()
    var usersLiveData = MutableLiveData<List<UserEntity>>()

    var stateList = arrayListOf<String>()
    var districtList = arrayListOf<String>()
    var cityList = arrayListOf<String>()

    var stateMap = hashMapOf<String, Int>()
    var districtMap = hashMapOf<String, Int>()



    fun getStateData(){
        compositeDisposable.add(mainRepository.getStateData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val list=it.Data.STATE
                for (i in list.indices){
                    stateList.add(list[i].StateName)
                    stateMap[list[i].StateName] = list[i].StateID
                }
                stateListLiveData.postValue(stateList)
                stateMapLiveData.postValue(stateMap)
            }
            .subscribe(
                {

                },
                {
                    Log.e(TAG,it.message.toString());
                }
            )
        )
    }

    fun getDistrictData(stateId: Int?) {
        compositeDisposable.add(mainRepository.getDistrictData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                val list=it.Data.DISTRICT
                if (districtList.size>0) {
                    districtList.clear()
                    districtMap.clear()
                }
                for (i in list.indices){
                    if (list[i].StateID==stateId){
                        val dname=list[i].DistrictName
                        districtList.add(dname)
                        districtMap[dname] = list[i].DistrictID
                    }
                }

                districtListLiveData.postValue(districtList)
                districtMapLiveData.postValue(districtMap)
            }.subscribe(
                {

                },
                {
                    Log.e(TAG,it.message.toString());
                }
            )
        )
    }

    fun getCitiesByDistrictId(districtId: Int?) {
        compositeDisposable.add(mainRepository.getCityData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                if (cityList.size>0) cityList.clear()
                val list=it.Data.CITY
                for (c in list){
                    if (c.DistrictID==districtId){
                        cityList.add(c.CityName)
                    }
                }
                cityListLiveData.postValue(cityList)
            }.subscribe()
        )
    }

    fun insertUser(userEntity: UserEntity){
        mainRepository.insertUser(userEntity)
      /*compositeDisposableRx2.add(mainRepository.getCount()
            .map {
                if (it==0){
                    mainRepository.insertUser(userEntity)
                }
            }
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(
                {
                    Log.d(TAG,"inserted")
                 },
                {
                    Log.d(TAG,it.message.toString())
                }
            )
      )*/
    }
    private val mTrigger  = MutableLiveData<Boolean>()
    private val users : LiveData<List<UserEntity>> = Transformations.switchMap(mTrigger){
        mainRepository.getAllUser()
    }
    fun getAllUsers(): LiveData<List<UserEntity>>{
        Log.e(TAG,"Successfully got all users \n $users")
        return users
       /* compositeDisposableRx2.add(mainRepository.getAllUser()
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .subscribe(
                {
                    usersLiveData.postValue(it)
                    Log.e(TAG,"Successfully got all users \n $it")
                },
                {
                    Log.e(TAG,it.message.toString())
                }
            )
        )*/
    }


    //paging
    private val pageListConfig = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setInitialLoadSizeHint(INITIAL_LOAD_SIZE)
        .setPageSize(PAGE_SIZE)
        .build()

   /* val userPagedList : LiveData<PagedList<UserEntity>> =
        LivePagedListBuilder(mainRepository.allUSers(), pageListConfig).build()*/


    val userPagedList : LiveData<PagedList<UserEntity>> =
        LivePagedListBuilder(dataSourceFactory, pageListConfig).build()


    //Searching
    val userSearchListLiveData= MutableLiveData<List<UserEntity>>()
    private val publishSubject: PublishSubject<String> = PublishSubject.create()

    fun searchUsers(searchView: EditText) {
        compositeDisposableRx2.add(
            RxTextView.textChangeEvents(searchView)
                .skipInitialValue()
                .debounce(300,TimeUnit.MILLISECONDS)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribeWith(searchUsersTextWatcher())
        )
        compositeDisposableRx2.add(
            publishSubject.debounce(300, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .switchMapSingle {
                    return@switchMapSingle mainRepository.searchUsers(it)
                        .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                        .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                }.subscribeWith(
                    getSearchObserver()
                )
        )
    }

    private fun searchUsersTextWatcher() : DisposableObserver<TextViewTextChangeEvent>{
        return object :DisposableObserver<TextViewTextChangeEvent>(){
            override fun onComplete() {

            }

            override fun onNext(t: TextViewTextChangeEvent) {
                Log.e(TAG, "searchContactsTextWatcher \nonNext ${t.text()}")
               publishSubject.onNext(t.text().toString())
            }

            override fun onError(e: Throwable) {
                Log.e(TAG,"searchContactsTextWatcher onError : ${e.message.toString()}")
            }

        }
    }

    private fun getSearchObserver() : DisposableObserver<List<UserEntity>>{
        return object : DisposableObserver<List<UserEntity>>(){
            override fun onComplete() {
                Log.e(TAG, "getSearchObserver onComplete")
            }

            override fun onNext(t: List<UserEntity>) {
                Log.e(TAG, "getSearchObserver \nonNext ${t.size}")
                userSearchListLiveData.postValue(t)
            }

            override fun onError(e: Throwable) {
                Log.e(TAG,"getSearchObserver onError : ${e.message.toString()}")
            }
        }
    }




    fun loadUsers(){
        mTrigger.value=true
    }

    fun onDestroy(){
        compositeDisposable.dispose()
        compositeDisposableRx2.dispose()
    }




}