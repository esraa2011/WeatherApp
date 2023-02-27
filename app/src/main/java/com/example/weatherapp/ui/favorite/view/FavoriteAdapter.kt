package com.example.weatherapp.ui.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DailyItemBinding
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.FavoriteWeatherPlacesModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FavoriteAdapter(
    private val favoriteWeatherPlaces: List<FavoriteWeatherPlacesModel>,
    var listener: (FavoriteWeatherPlacesModel) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    private lateinit var binding: FavItemBinding

    class ViewHolder(var binding: FavItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = FavItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = favoriteWeatherPlaces.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.FavoritePlaceName.text = favoriteWeatherPlaces[position].locationName
        holder.binding.deleteFromFav.setOnClickListener {
            favoriteWeatherPlaces[position]?.let { it -> listener(it) }
        }

    }

}
