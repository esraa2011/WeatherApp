package com.example.weatherapp.ui.favorite.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.models.FavoriteWeatherPlacesModel
import com.example.weatherapp.databinding.FavItemBinding

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
