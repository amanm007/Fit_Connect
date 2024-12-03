package com.example.fit_connect.ui.rewards

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fit_connect.R
import android.widget.ImageView
import android.widget.TextView

class RewardsPagerAdapter(
    private val rewards: List<RewardData>
) : RecyclerView.Adapter<RewardsPagerAdapter.RewardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RewardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_reward_item, parent, false)
        return RewardViewHolder(view)
    }

    override fun onBindViewHolder(holder: RewardViewHolder, position: Int) {
        val reward = rewards[position]
        holder.bind(reward)
    }

    override fun getItemCount(): Int = rewards.size

    class RewardViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(reward: RewardData) {
            val rewardTitle = view.findViewById<TextView>(R.id.rewardTitle)
            val rewardDescription = view.findViewById<TextView>(R.id.rewardDescription)
            val rewardImage = view.findViewById<ImageView>(R.id.rewardImage)

            rewardTitle.text = reward.title
            rewardDescription.text = reward.description
            rewardImage.setImageResource(reward.imageRes)
        }
    }
}
