package com.example.fit_connect.ui.home.nested_fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.fit_connect.R
import com.example.fit_connect.databinding.FragmentNestedPrhistoryBinding
import com.example.fit_connect.ui.home.nested_fragments.listadapters.PRListAdapter


class NestedPRHistoryFragment: Fragment() {

    private var _binding: FragmentNestedPrhistoryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentNestedPrhistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root


        setPRHistory(root)
        return root
    }
    private fun setPRHistory(root : View){
        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        for(i in 1..10) arrayList.add(i)

        val arrayAdapter = PRListAdapter(requireActivity(), arrayList)
        val prListView : ListView = root.findViewById(R.id.pr_history_listview)
        prListView.adapter = arrayAdapter


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}