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
import com.example.fit_connect.databinding.FragmentNestedFollowingBinding
import com.example.fit_connect.ui.home.nested_fragments.listadapters.FollowingListAdapter

class NestedFollowingFragment : Fragment() {

    private var _binding: FragmentNestedFollowingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var followingListView : ListView
    private val ACTIVITY_REQUEST_CODE = 1000000

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentNestedFollowingBinding.inflate(inflater,container,false)
        val root: View = binding.root
        followingListView = root.findViewById(R.id.following_listview)
        setFollowList()

        return root
    }

    private fun setFollowList() {
        //Replace array with actual database array
        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        arrayList.add(1)
        arrayList.add(2)

        val arrayAdapter = FollowingListAdapter(requireActivity(), arrayList)
        followingListView.adapter = arrayAdapter
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