package com.example.fit_connect.ui.home.nested_fragments
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentNestedFollowingBinding.inflate(inflater,container,false)
        val root: View = binding.root

        setFollowList(root)

        return root
    }

    private fun setFollowList(root: View) {
        //Replace array with actual database array
        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        arrayList.add(1)
        arrayList.add(2)

        val arrayAdapter = FollowingListAdapter(requireActivity(), arrayList)
        val followingListView : ListView = root.findViewById(R.id.following_listview)
        followingListView.adapter = arrayAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}