package com.example.weatherapp.ui.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DailyItemBinding
import com.example.weatherapp.databinding.FavItemBinding
import com.example.weatherapp.data.models.Daily
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FavoriteAdapter(
    private var favoriteWeatherPlaces: List<FavoriteWeatherPlacesModel>,
    var deleteAction: (FavoriteWeatherPlacesModel) -> Unit,
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
        var current = favoriteWeatherPlaces[position]
        holder.binding.FavoritePlaceName.text = favoriteWeatherPlaces[position].locationName
        holder.binding.deleteFromFav.setOnClickListener {
            deleteAction(current)
        }
        holder.binding.itemView.setOnClickListener {
            listener(current)
        }
    }

}
