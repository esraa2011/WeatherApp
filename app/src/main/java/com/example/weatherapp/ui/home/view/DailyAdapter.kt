package com.example.weatherapp.ui.home.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.weatherapp.R
import com.example.weatherapp.databinding.DailyItemBinding
import com.example.weatherapp.models.Daily
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DailyAdapter(private val daily: List<Daily>) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    private lateinit var binding: DailyItemBinding

    class ViewHolder(var binding: DailyItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DailyItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = daily.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentDay = daily[position]


        val current = daily[position]
        holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
        holder.binding.weatherStateInDaily.text = current.weather[0].description
        with(holder) {
            holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
        }
        holder.binding.dailyTemp.text =
            "${current.temp.min.toInt()} / ${current.temp.max.toInt()} °C"

    }


}
