package com.example.fit_connect.ui.home.nested_fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fit_connect.databinding.FragmentNestedFollowingBinding

class NestedFollowingFragment : Fragment() {

    private var _binding: FragmentNestedFollowingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentNestedFollowingBinding.inflate(inflater,container,false)
        val root: View = binding.root


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}