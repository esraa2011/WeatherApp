package com.example.weatherapp.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.DailyItemBinding
import com.example.weatherapp.databinding.TempItemBinding
import com.example.weatherapp.models.Current
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.Weather

class HoursAdapter(private val weather: List<Current>) :
    RecyclerView.Adapter<HoursAdapter.ViewHolder>() {
    private lateinit var binding: TempItemBinding

    class ViewHolder(var binding: TempItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = TempItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = weather.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

        with(holder) {
            holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
        }
        holder.binding.hourTemp.text =
            "${weather.get(position).temp.toInt()}  °C"


    }


}

