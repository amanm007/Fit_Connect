package com.example.fit_connect.ui.home.nested_fragments
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.fit_connect.R
import com.example.fit_connect.data.FitConnectDatabase
import com.example.fit_connect.data.user.Following
import com.example.fit_connect.data.user.UserRepository
import com.example.fit_connect.databinding.FragmentNestedFollowingBinding
import com.example.fit_connect.ui.UserActivity
import com.example.fit_connect.ui.home.nested_fragments.listadapters.FollowingListAdapter

class NestedFollowingFragment : Fragment() {

    private var _binding: FragmentNestedFollowingBinding? = null
    private val user_id = "USER_ID"
    private var userId :Long = -1
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var followingListView : ListView
    private val ACTIVITY_REQUEST_CODE = 1000000

    //List
    private lateinit var followingList : List<Following>
    private lateinit var followingArrayAdapter : FollowingListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val bundle = (activity as? UserActivity)?.sharedBundle
        userId = bundle!!.getLong(user_id)

        _binding = FragmentNestedFollowingBinding.inflate(inflater,container,false)
        val root: View = binding.root
        followingListView = root.findViewById(R.id.following_listview)

        println(userId)
        if(userId != -1L) {
            setFollowList()
        }
        return root
    }

    private fun setFollowList() {
        //Replace array with actual database array

        val database = FitConnectDatabase.getInstance(requireActivity())
        val userDatabaseDao = database.userDao()
        val repository = UserRepository(userDatabaseDao)

        val getFollowersLiveData = repository.getUserWithFollowing(userId)

        followingList = listOf()
        followingArrayAdapter = FollowingListAdapter(requireActivity(),followingList)
        followingListView.adapter = followingArrayAdapter

        getFollowersLiveData.observe(requireActivity(),{ Followers->
            followingArrayAdapter.replace(Followers.followings)
            followingArrayAdapter.notifyDataSetChanged()
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000000){
            if(resultCode == Activity.RESULT_OK){
                setFollowList()
                //Refresh List with new comments + Last Comment Made
            }
        }
    }
}