package com.example.weatherapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity
data class AlarmPojo(
    var alarmTitle : String,
    var alarmStartDate : Long,
    var alarmEndDate : Long,
    var alarmTime : Long,
    var alarmType : String,
    var alarmDays : List<String>,
    var isNotification : Boolean
) : Serializable{
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id : Int = 0
}
