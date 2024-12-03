package com.example.fit_connect.ui.dashboard.measurements

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import java.text.SimpleDateFormat
import java.util.*

class MeasurementAdapter : ListAdapter<Measurement, MeasurementAdapter.MeasurementViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasurementViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_measurement, parent, false)
        return MeasurementViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeasurementViewHolder, position: Int) {
        val measurement = getItem(position) // Fetch item from the list
        holder.bind(measurement)
    }

    class MeasurementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(measurement: Measurement) {
            itemView.findViewById<TextView>(R.id.dateTextView).text =
                SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(measurement.date))

            itemView.findViewById<TextView>(R.id.bodyWeightTextView).text =
                "Weight: ${measurement.bodyWeight ?: "-"} lbs"

            itemView.findViewById<TextView>(R.id.waistTextView).text =
                "Waist: ${measurement.waist ?: "-"} cm"


            itemView.findViewById<TextView>(R.id.neckTextView).text =
                "Neck: ${measurement.neck ?: "-"} cm"
            itemView.findViewById<TextView>(R.id.shoulderTextView).text =
                "Shoulder: ${measurement.shoulder ?: "-"} cm"
            itemView.findViewById<TextView>(R.id.chestTextView).text =
                "Chest: ${measurement.chest ?: "-"} cm"
            itemView.findViewById<TextView>(R.id.bicepTextView).text =
                "Bicep: ${measurement.bicep ?: "-"} cm"
            itemView.findViewById<TextView>(R.id.forearmTextView).text =
                "Forearm: ${measurement.Forearm ?: "-"} cm"
            itemView.findViewById<TextView>(R.id.abdomenTextView).text =
                "Abdomen: ${measurement.abdomen ?: "-"} cm"
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Measurement>() {
        override fun areItemsTheSame(oldItem: Measurement, newItem: Measurement) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Measurement, newItem: Measurement) = oldItem == newItem
    }
}
