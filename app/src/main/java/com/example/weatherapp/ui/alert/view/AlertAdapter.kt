package com.example.weatherapp.ui.alert.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.data.models.AlarmPojo
import com.example.weatherapp.data.models.Utility
import com.example.weatherapp.databinding.AlertItemBinding

class AlertAdapter(
    private var alert: List<AlarmPojo>,
    var deleteAction: (AlarmPojo) -> Unit
) : RecyclerView.Adapter<AlertAdapter.ViewHolder>() {
    private lateinit var binding: AlertItemBinding

    class ViewHolder(var binding: AlertItemBinding) : RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = AlertItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = alert.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var currentAlert = alert[position]
        holder.binding.tvStartDay.text =
            alert[position].alarmStartDay?.let { Utility.longToDate(it) }
        holder.binding.tvEndDay.text = alert[position].alarmEndDay?.let { Utility.longToDate(it) }

        holder.binding.tvStartTime.text = alert[position].alarmStartTime?.let {
            Utility.timeStampToHour(
                it
            )
        }
        holder.binding.tvEndTime.text = alert[position].alarmEndTime?.let {
            Utility.timeStampToHour(
                it
            )
        }
        holder.binding.deleteAlert.setOnClickListener {
            deleteAction(currentAlert)
        }
    }


}