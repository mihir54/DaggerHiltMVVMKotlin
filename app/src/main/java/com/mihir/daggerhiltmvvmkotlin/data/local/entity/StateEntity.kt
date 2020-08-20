package com.mihir.daggerhiltmvvmkotlin.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "states",
    foreignKeys = [
    ForeignKey(
        entity = DistrictEntity::class,
        parentColumns = ["id"],
        childColumns = ["districtId"],
        onDelete = ForeignKey.CASCADE
    )]

)
data class StateEntity(

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0,

    @ColumnInfo(name = "stateId")
    var stateId : Int,

    @ColumnInfo(name= "districtId")
    var districtId : Int,

    @ColumnInfo(name = "stateName")
    var stateName : String
) {
    constructor() : this(0,0,0,"")
}