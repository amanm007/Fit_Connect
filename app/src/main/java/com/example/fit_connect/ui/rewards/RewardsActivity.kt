package com.example.fit_connect.ui.rewards

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.fit_connect.R
import com.example.fit_connect.ui.rewards.RewardsPagerAdapter
import com.example.fit_connect.ui.rewards.RewardData

class RewardsActivity : AppCompatActivity() {

    private lateinit var rewardsViewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rewards)

        // Initialize ViewPager2
        rewardsViewPager = findViewById(R.id.rewardsViewPager)

        // Populate with fake data for now
        val rewardsList = listOf(
            RewardData("Heaviest Lift", "225 lbs - Bench Press", R.drawable.ic_medal),
            RewardData("Weekly lift", " Keep it going!", R.drawable.ic_fire),
            RewardData("Total Volume", " lifted last week", R.drawable.ic_truck),
            RewardData("Workout Duration", "1 hr 57 mins, 7 exercises, 33 sets", R.drawable.ic_stats)
        )

        // Set up adapter
        val adapter = RewardsPagerAdapter(rewardsList)
        rewardsViewPager.adapter = adapter

        // Done Button to exit the activity
        findViewById<Button>(R.id.buttonDone).setOnClickListener {
            finish() // Close the Rewards screen
        }
    }
}
