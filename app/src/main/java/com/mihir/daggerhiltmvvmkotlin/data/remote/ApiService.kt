package com.mihir.daggerhiltmvvmkotlin.data.remote

import com.mihir.daggerhiltmvvmkotlin.data.remote.response.city.CityResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.DistrictResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.state.StateResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET

interface ApiService {

    @GET(Endpoints.GET_CITY)
    fun getCities() : Single<CityResponse>
    @GET(Endpoints.GET_DISTRICT)
    fun getDistricts() : Single<DistrictResponse>
    @GET(Endpoints.GET_STATE)
    fun getStates() : Single<StateResponse>
}