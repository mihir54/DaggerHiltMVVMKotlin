package com.mihir.daggerhiltmvvmkotlin.data.local.entity

import android.os.Parcelable
import android.text.Editable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

@Parcelize
@Entity(tableName = "user")
data class UserEntity (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id : Long=0,

    @ColumnInfo(name = "name")
    val name: String?,

    @ColumnInfo(name = "mobile_no")
    val mobile_no : Long?,

    @ColumnInfo(name = "address")
    val address : String?,

    @ColumnInfo(name = "state")
    val state : String?,

    @ColumnInfo(name = "district")
    val district : String?,

    @ColumnInfo(name = "city")
    val city : String?,

    @ColumnInfo(name= "imageUri")
    val imageUri : String?

):Parcelable {
    override fun toString(): String {
        return "UserEntity(id=$id, name='$name', mobile_no=$mobile_no, address='$address', state='$state', district='$district', city='$city')"
    }
}