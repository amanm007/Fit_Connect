package com.example.fit_connect.ui.dashboard.measurements

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fit_connect.R

class AddMeasurementFragment : Fragment(R.layout.fragment_add_measurement) {
    private val viewModel: MeasurementViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.saveButton).setOnClickListener {
            val bodyWeight = view.findViewById<EditText>(R.id.bodyWeightInput).text.toString().toFloatOrNull()
            val waist = view.findViewById<EditText>(R.id.waistInput).text.toString().toFloatOrNull()
            val bodyFat = view.findViewById<EditText>(R.id.bodyFatInput).text.toString().toFloatOrNull()
            val neck = view.findViewById<EditText>(R.id.neckInput).text.toString().toFloatOrNull()
            val shoulder = view.findViewById<EditText>(R.id.shoulderInput).text.toString().toFloatOrNull()
            val chest = view.findViewById<EditText>(R.id.chestInput).text.toString().toFloatOrNull()
            val bicep = view.findViewById<EditText>(R.id.bicepInput).text.toString().toFloatOrNull()
            val Forearm = view.findViewById<EditText>(R.id.forearmInput).text.toString().toFloatOrNull()
            val abdomen = view.findViewById<EditText>(R.id.abdomenInput).text.toString().toFloatOrNull()

            val measurement = Measurement(
                date = System.currentTimeMillis(),
                bodyWeight = bodyWeight,
                waist = waist,
                bodyFat = bodyFat,
                neck = neck,
                shoulder = shoulder,
                chest = chest,
                bicep = bicep,
                Forearm = Forearm,
                abdomen = abdomen
            )
            viewModel.insertMeasurement(measurement)
            findNavController().navigateUp()
        }
    }
}
