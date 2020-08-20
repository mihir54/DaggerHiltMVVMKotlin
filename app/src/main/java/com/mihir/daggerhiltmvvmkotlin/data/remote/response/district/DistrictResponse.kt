package com.mihir.daggerhiltmvvmkotlin.data.remote.response.district

import com.mihir.daggerhiltmvvmkotlin.data.remote.response.district.Data

data class DistrictResponse(
    val Data: Data,
    val Message: String,
    val MsgCode: Int
)