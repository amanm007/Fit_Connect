package com.example.fit_connect.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.fit_connect.R
import com.example.fit_connect.databinding.FragmentHomeBinding
import com.example.fit_connect.ui.UserActivity
import com.example.fit_connect.ui.home.nested_fragments.following_activities.FindFollowerActivity
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val user_id = "USER_ID"
    private var userId : Long = 0

    //Find Followers Btn
    private lateinit var findFollowerBtn : ImageButton

    // This property is only valid between onCreateView and
    // onDestroyView.


    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Get User Id
        val bundle = (activity as? UserActivity)?.sharedBundle
        userId = bundle!!.getLong(user_id)

        setFindFollowerButton(root)
        setNavigationListener(root)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFindFollowerButton(root: View){
        findFollowerBtn = root.findViewById(R.id.find_followers_home_btn)
        findFollowerBtn.setOnClickListener(){
            val intent = Intent(context, FindFollowerActivity::class.java)
            intent.putExtra(user_id, userId)
            startActivity(intent)
        }
    }

    private fun setNavigationListener(root : View){
        val navController = childFragmentManager.findFragmentById(R.id.nav_host_fragment_content_home)
            ?.let{ it as NavHostFragment }?.navController
        val spinner: Spinner = root.findViewById(R.id.homebar_spinner)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                // Get the selected item
                if(position == 0) navController?.navigate(R.id.nav_nested_home)
                else if(position==1) navController?.navigate(R.id.nav_nested_following)
                else navController?.navigate(R.id.nav_nested_prhistory)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle the case where no item is selected
            }
        }
    }
}