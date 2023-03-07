package com.example.weatherapp.ui.home.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.databinding.DailyItemBinding
import com.example.weatherapp.models.Daily
import com.example.weatherapp.models.Utility

class DailyAdapter(private val daily: List<Daily>, var context: Context?) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    private lateinit var binding: DailyItemBinding

    var languageShared = context?.getSharedPreferences("Language", Context.MODE_PRIVATE)
    var langu = languageShared?.getString(Utility.Language_Key, "en")!!
    var unitsShared = context?.getSharedPreferences("Units", Context.MODE_PRIVATE)
    var unit = unitsShared?.getString(Utility.TEMP_KEY, "metric")!!

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



        if (langu == "en" && unit == "metric") {
            holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
            holder.binding.weatherStateInDaily.text = current.weather[0].description
            with(holder) {
                holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
            }
            holder.binding.dailyTemp.text =
                "${current.temp.min.toInt()} / ${current.temp.max.toInt()} °C"
        } else if (langu == "ar" && unit == "metric") {
            holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
            holder.binding.weatherStateInDaily.text = current.weather[0].description
            with(holder) {
                holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
            }
            holder.binding.dailyTemp.text =
                "${Utility.convertNumbersToArabic(current.temp.max.toInt())} / ${
                    Utility.convertNumbersToArabic(current.temp.max.toInt())
                } س° "
        } else if (langu == "en" && unit == "imperial") {
            holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
            holder.binding.weatherStateInDaily.text = current.weather[0].description
            with(holder) {
                holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
            }
            holder.binding.dailyTemp.text =
                "${current.temp.max.toInt()} / ${current.temp.max.toInt()} ℉"
        } else if (langu == "ar" && unit == "imperial") {
            holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
            holder.binding.weatherStateInDaily.text = current.weather[0].description
            with(holder) {
                holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
            }
            holder.binding.dailyTemp.text =
                "${Utility.convertNumbersToArabic(current.temp.max.toInt())} / ${
                    Utility.convertNumbersToArabic(current.temp.max.toInt())
                }ف° "
        } else if (langu == "en" && unit == "standard") {
            holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
            holder.binding.weatherStateInDaily.text = current.weather[0].description
            with(holder) {
                holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
            }
            holder.binding.dailyTemp.text =
                "${current.temp.max.toInt()} / ${current.temp.max.toInt()} °K"
        } else if (langu == "ar" && unit == "standard") {
            holder.binding.dayOfWeek.text = Utility.timeStampToDay(current.dt)
            holder.binding.weatherStateInDaily.text = current.weather[0].description
            with(holder) {
                holder.binding.weekDayTempStatusIcon.setImageResource(Utility.getWeatherIcon(daily[position].weather[0].icon))
            }
            holder.binding.dailyTemp.text =
                "${Utility.convertNumbersToArabic(current.temp.max.toInt())} / ${
                    Utility.convertNumbersToArabic(current.temp.max.toInt())
                } ك° "
        }


//        holder.binding.dailyTemp.text =
//            "${current.temp.min.toInt()} / ${current.temp.max.toInt()} °C"

    }


}
