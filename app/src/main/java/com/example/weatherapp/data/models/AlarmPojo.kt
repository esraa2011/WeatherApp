package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity
data class AlarmPojo(
    var alarmStartDay: Long,
    var alarmEndDay: Long,
    var alarmStartTime: String,
    var alarmEndTime: String,
    var areaName: String

) : Serializable {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0
}
