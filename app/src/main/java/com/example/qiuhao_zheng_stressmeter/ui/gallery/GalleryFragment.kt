package com.example.qiuhao_zheng_stressmeter.ui.gallery

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.example.qiuhao_zheng_stressmeter.R
import com.example.qiuhao_zheng_stressmeter.databinding.FragmentGalleryBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import java.io.File



class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // read data from csv file
        val (time, stressData) = readData()

        setChart(stressData)
        setTable(time, stressData)
//        setChart(stressData)

        val result = Bundle().apply {
            putBoolean("isFirstTime", false)
        }
        setFragmentResult("updateIsFirstTimeKey", result)

        return root
    }

    // read data from csv file
    private fun readData(): Pair<List<String>, List<Int>>  {
        val time = mutableListOf<String>()
        val stressData = mutableListOf<Int>()
        try {
            val file = File(context?.getExternalFilesDir(null), "stress.csv")
            val fileContent = file.readText()

            fileContent.split("\n").forEach { line ->
                if (line.isNotBlank()) {
                    val parts = line.split(",").map { it.trim() }
                    time.add(parts[0])
                    stressData.add(parts[1].toIntOrNull() ?: 0)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(time, stressData)
    }

    // plot chart for the data
    private fun setChart(stressData: List<Int>) {
        val lineChart: LineChart = binding.lineChart

        val data = stressData.mapIndexed { index, value ->
            Entry(index.toFloat(), value.toFloat())
        }

        val dataSet = LineDataSet(data, "Stress Level")
        dataSet.color = Color.parseColor("#4E5EFF")
        dataSet.setCircleColor(Color.parseColor("#008080"))
        dataSet.lineWidth = 3f
        dataSet.setDrawValues(false)
        // Create a curved line
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        // Set up fill color
        dataSet.setDrawFilled(true)
        val colorSet = Color.parseColor("#70B5D7F6")
        dataSet.fillColor = colorSet

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        // x axis label
        val description = Description()
        description.text = "Instances"
        description.textSize = 13f
        description.textColor = Color.BLACK
        lineChart.description = description
        lineChart.description.isEnabled = true

        // y axis label
        val legend = lineChart.legend
        legend.isEnabled = true
        legend.textColor = Color.BLACK
        legend.textSize = 13f
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT

        val xAxis = lineChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f

        val yAxis = lineChart.axisLeft
        yAxis.setDrawGridLines(true)
        yAxis.axisMinimum = 0f
        // Disable right Y-axis
        lineChart.axisRight.isEnabled = false

        lineChart.invalidate()
    }

    // make a table for the data
    private fun setTable(times: List<String>, stressLevels: List<Int>) {
        val tableLayout: TableLayout = binding.table
        // set up the header row
        val headerRow = TableRow(context).apply {
            addView(TextView(context).apply {
                text = "Time"
                setTypeface(null, Typeface.BOLD)
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                textSize = 18f
                setBackgroundResource(R.drawable.headerborder)
            })
            addView(TextView(context).apply {
                text = "Stress"
                setTypeface(null, Typeface.BOLD)
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                textSize = 18f
                setBackgroundResource(R.drawable.headerborder)
            })
        }
        tableLayout.addView(headerRow)
        // set up the table
        times.zip(stressLevels).forEach { (time, stress) ->
            val tableRow = TableRow(context)
            tableRow.addView(TextView(context).apply {
                text = time
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                textSize = 16f
                setBackgroundResource(R.drawable.border)
            })
            tableRow.addView(TextView(context).apply {
                text = stress.toString()
                gravity = Gravity.CENTER
                setTextColor(Color.BLACK)
                textSize = 16f
                setBackgroundResource(R.drawable.border)
            })
            tableLayout.addView(tableRow)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



