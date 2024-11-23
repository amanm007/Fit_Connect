package com.example.fit_connect.ui.home.nested_fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.enums.LegendLayout
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.example.fit_connect.R
import com.example.fit_connect.databinding.FragmentNestedHomeBinding
import com.example.fit_connect.ui.home.nested_fragments.listadapters.PRListAdapter


class NestedHomeFragment: Fragment() {

    private var _binding: FragmentNestedHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        _binding = FragmentNestedHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        calculateWeeklyStreak()
        setFitGroupTrack(root)
        setPRHistory(root)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun calculateWeeklyStreak(){

    }

    private fun setFitGroupTrack(root : View){
        val data = listOf(
            ValueDataEntry(0,2),
            ValueDataEntry(1,4),
            ValueDataEntry(2,2),
            ValueDataEntry(3,5),
            ValueDataEntry(4,3.5)
        )
        val data2 = listOf(
            ValueDataEntry(0,1),
            ValueDataEntry(1,2),
            ValueDataEntry(2,3),
            ValueDataEntry(3,5),
            ValueDataEntry(4,4)
        )

        val chartView = root.findViewById<AnyChartView>(R.id.activity_chart_view)
        val cart : Cartesian = AnyChart.line()

        cart.tooltip().positionMode(TooltipPositionMode.POINT)

        val line: Line = cart.line(data)
        val line2: Line = cart.line(data2)

        line.stroke("2 #000000")
        line.stroke("2 #FF0000")

        line.markers().enabled(true)
        line2.markers().enabled(true)

        line.markers().type(MarkerType.CIRCLE).size(3).fill("#FF0000").stroke("#FF0000")
        line2.markers().type(MarkerType.CIRCLE).size(3).fill("#0000FF").stroke("#0000FF")

        cart.legend().itemsLayout("horizantal").align("right").fontSize(10)
        cart.legend(true)
        cart.legend().iconSize(10)
        cart.legend().itemsLayout(LegendLayout.HORIZONTAL)

        cart.xGrid(0).enabled(true)
        cart.xAxis(0).labels().fontSize(10)

        cart.yAxis(0).title("Hours")

        chartView.setChart(cart)
    }

    private fun setPRHistory(root : View){
        val arrayList : ArrayList<Int> = java.util.ArrayList(3)
        arrayList.add(1)
        arrayList.add(2)
        arrayList.add(3)

        val arrayAdapter = PRListAdapter(requireActivity(), arrayList)
        val prListView : ListView = root.findViewById(R.id.pr_recent_list)
        prListView.adapter = arrayAdapter

    }
}