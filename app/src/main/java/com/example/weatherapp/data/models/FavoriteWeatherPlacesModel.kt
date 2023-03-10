package com.example.weatherapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.io.Serializable

@Entity(tableName = "FavoriteWeatherPlacesModel")
data class FavoriteWeatherPlacesModel(
    var locationName: String,
    var lat: Double,
    var lon: Double

) : Serializable {
    @NotNull
    @PrimaryKey(autoGenerate = true)
    var favoriteId: Int = 0
}
