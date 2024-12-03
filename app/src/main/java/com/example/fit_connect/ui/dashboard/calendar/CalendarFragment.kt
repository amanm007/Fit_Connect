package com.example.fit_connect.ui.dashboard.calendar

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.workout.WorkoutRepository
import com.example.fit_connect.databinding.FragmentCalendarBinding
import com.example.fit_connect.ui.UserActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment(R.layout.fragment_calendar) {
    private val user_id = "USER_ID"

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentCalendarBinding.bind(view)

        val db = FitConnectDatabase.getInstance(requireContext())
        val workoutRepo = WorkoutRepository(db.workoutDao())

        val bundle = (activity as? UserActivity)?.sharedBundle
        val userId = bundle!!.getLong(user_id)

        val calendarView = binding.calendarView
        calendarView.selectionMode = MaterialCalendarView.SELECTION_MODE_SINGLE

        // Highlight the current day
        val currentDate = CalendarDay.today()
        calendarView.addDecorator(CurrentDayDecorator(currentDate))

        // Set month label
        updateMonthLabel(calendarView.currentDate)

        // Listen for month changes
        calendarView.setOnMonthChangedListener { _, date ->
            updateMonthLabel(date)
        }

        workoutRepo.getUserWorkoutDays(userId).observe(viewLifecycleOwner) { workoutDays ->
            workoutDays
                .map { CalendarDay.from(it.year, it.monthValue, it.dayOfMonth) }
                .forEach { calendarView.setDateSelected(it, true) }
        }

        // Highlight example dates
//        val highlightedDates = listOf(
//            CalendarDay.from(2024, 10, 3),
//            CalendarDay.from(2024, 10, 8),
//            CalendarDay.from(2024, 10, 14),
//            CalendarDay.from(2024, 10, 17),
//            CalendarDay.from(2024, 10, 21),
//            CalendarDay.from(2024, 10, 23),
//            CalendarDay.from(2024, 11, 4),
//            CalendarDay.from(2024, 11, 6),
//            CalendarDay.from(2024, 11, 9)
//        )
//        highlightedDates.forEach { date ->
//            calendarView.setDateSelected(date, true)
//        }

        // Update streak and rest buttons
        binding.streakButton.text = "3 weeks Streak"
        binding.restButton.text = "1 day Rest"

        // Back button listener
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun updateMonthLabel(date: CalendarDay) {
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.set(date.year, date.month - 1, 1)
        val monthText = monthFormat.format(calendar.time)
        binding.monthLabel.text = monthText
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Decorator for highlighting the current day
    inner class CurrentDayDecorator(private val date: CalendarDay) : DayViewDecorator {
        override fun shouldDecorate(day: CalendarDay): Boolean {
            return day == date
        }

        override fun decorate(view: DayViewFacade) {
            view.setBackgroundDrawable(requireContext().getDrawable(R.drawable.circle_today)!!)
            view.addSpan { textPaint: android.text.TextPaint ->
                textPaint.color = resources.getColor(R.color.blue, null)
            }
        }
    }
}
