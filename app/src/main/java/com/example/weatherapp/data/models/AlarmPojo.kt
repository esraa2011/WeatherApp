package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity
data class AlarmPojo(
    var alarmStartDay: Long?=null,
    var alarmEndDay: Long?=null,
    var alarmStartTime: Long?=null,
    var alarmEndTime: Long?=null,
    var areaName: String?=null,
    var latitude: Double?=null,
    var longitude: Double?=null

) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0
}
