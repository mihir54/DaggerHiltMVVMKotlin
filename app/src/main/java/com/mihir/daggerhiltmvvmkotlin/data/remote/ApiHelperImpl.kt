package com.mihir.daggerhiltmvvmkotlin.data.remote

import com.mihir.daggerhiltmvvmkotlin.data.remote.response.city.CityResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.DistrictResponse
import com.mihir.daggerhiltmvvmkotlin.data.remote.response.state.StateResponse
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper {
    override fun getCities(): Single<CityResponse> = apiService.getCities()

    override fun getDistricts(): Single<DistrictResponse> = apiService.getDistricts()

    override fun getStates(): Single<StateResponse> = apiService.getStates()
}