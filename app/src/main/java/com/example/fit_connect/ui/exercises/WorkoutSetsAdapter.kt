package com.example.fit_connect.ui.exercises

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import com.example.fit_connect.databinding.ItemWorkoutSetBinding

class WorkoutSetsAdapter : RecyclerView.Adapter<WorkoutSetsAdapter.SetViewHolder>() {
    private val sets = mutableListOf<WorkoutSet>()

    inner class SetViewHolder(private val binding: ItemWorkoutSetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(set: WorkoutSet) {
            binding.apply {
                tvSetNumber.text = String.format(
                    binding.root.context.getString(R.string.set_number),
                    set.setNumber
                )

                tvWeightReps.text = String.format(
                    binding.root.context.getString(R.string.weight_reps_format),
                    set.weight,
                    set.reps
                )

                cbCompleted.isChecked = set.isCompleted

                cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                    val position = adapterPosition
                    if (position >= 0) {
                        sets[position].isCompleted = isChecked
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        return SetViewHolder(
            ItemWorkoutSetBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        holder.bind(sets[position])
    }

    override fun getItemCount() = sets.size

    fun addSet(weight: Float, reps: Int) {
        sets.add(WorkoutSet(sets.size + 1, weight, reps))
        notifyItemInserted(sets.size - 1)
    }
}