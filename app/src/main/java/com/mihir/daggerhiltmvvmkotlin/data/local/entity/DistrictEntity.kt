package com.mihir.daggerhiltmvvmkotlin.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "districts",
    foreignKeys = [
    ForeignKey(
        entity = CityEntity::class,
        parentColumns = ["id"],
        childColumns = ["cityId"],
        onDelete = ForeignKey.CASCADE
    )])
data class DistrictEntity (

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    @ColumnInfo(name = "districtId")
    var diatrictId : Int,

    @ColumnInfo(name="cityId")
    var cityId : Int,

    @ColumnInfo(name = "districtName")
    var districtName : String
)