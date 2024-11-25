package com.example.fit_connect.ui.dashboard.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R

data class CalendarDay(val day: Int, val isHighlighted: Boolean)

class CalendarAdapter(private val days: List<CalendarDay>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_calendar_day, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = days[position]
        holder.bind(day)
    }

    override fun getItemCount() = days.size

    class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dayTextView: TextView = itemView.findViewById(R.id.day_text_view)

        fun bind(day: CalendarDay) {
            dayTextView.text = day.day.toString()
            dayTextView.setBackgroundResource(
                if (day.isHighlighted) R.drawable.circle_highlighted else 0
            )
        }
    }
}