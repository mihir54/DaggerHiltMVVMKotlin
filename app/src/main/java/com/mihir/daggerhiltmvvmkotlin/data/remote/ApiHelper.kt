package com.mihir.daggerhiltmvvmkotlin.data.remote

import com.mihir.daggerhiltmvvmkotlin.data.remote.response.city.CityResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.DistrictResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.state.StateResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiHelper {
    fun getCities() : Single<CityResponse>
    fun getDistricts() : Single<DistrictResponse>
    fun getStates() : Single<StateResponse>
}