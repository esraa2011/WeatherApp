package com.example.weatherapp.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.TempItemBinding
import com.example.weatherapp.data.models.Current
import com.example.weatherapp.data.models.Utility

class HoursAdapter(private val weather: List<Current>, var context: Context?) :
    RecyclerView.Adapter<HoursAdapter.ViewHolder>() {
    private lateinit var binding: TempItemBinding
    var languageShared = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
    var langu = languageShared?.getString(Utility.Language_Key, "en")!!
    var unitsShared = context?.getSharedPreferences("Units", Context.MODE_PRIVATE)
    var unit = unitsShared?.getString(Utility.TEMP_KEY, "metric")!!

    class ViewHolder(var binding: TempItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = TempItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = weather.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {




        if (langu == "en" && unit == "metric") {
            binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

            with(holder) {
                holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
            }
            binding.hourTemp.text = "${weather.get(position).temp.toInt()}  °C"
        } else if (langu == "ar" && unit == "metric") {
            binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

            with(holder) {
                holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
            }
            binding.hourTemp.text =
                Utility.convertNumbersToArabic(weather.get(position).temp.toInt()) + " س°"
        } else if (langu == "en" && unit == "imperial") {
            binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

            with(holder) {
                holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
            }
            binding.hourTemp.text = "${weather.get(position).temp.toInt()}  ℉"
        } else if (langu == "ar" && unit == "imperial") {
            binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

            with(holder) {
                holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
            }
            binding.hourTemp.text =
                Utility.convertNumbersToArabic(weather.get(position).temp.toInt()) + "ف° "
        } else if (langu == "en" && unit == "standard") {
            binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

            with(holder) {
                holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
            }
            binding.hourTemp.text = "${weather.get(position).temp.toInt()} °K"
        } else if (langu == "ar" && unit == "standard") {
            binding.timeInTemp.text = Utility.timeStampToHour(weather[position].dt)

            with(holder) {
                holder.binding.hourImg.setImageResource(Utility.getWeatherIcon(weather[position].weather[0].icon))
            }
            binding.hourTemp.text =
                Utility.convertNumbersToArabic(weather.get(position).temp.toInt()) + " ك° "
        }

    }


}

