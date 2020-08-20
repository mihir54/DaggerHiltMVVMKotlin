package com.mihir.daggerhiltmvvmkotlin.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cities")
data class CityEntity(

    @PrimaryKey(autoGenerate = true)
    var id : Long=0,

    @ColumnInfo(name = "cityId")
    var cityId : Int,

    @ColumnInfo(name = "districtId")
    var districtId : Int,

    @ColumnInfo(name = "cityName")
    var cityName : String
)