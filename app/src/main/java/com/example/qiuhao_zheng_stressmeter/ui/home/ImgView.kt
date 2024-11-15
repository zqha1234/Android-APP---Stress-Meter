package com.example.qiuhao_zheng_stressmeter.ui.home

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.qiuhao_zheng_stressmeter.R
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.system.exitProcess

class ImgView : AppCompatActivity() {

    private val imageGroups = listOf(
        intArrayOf(
            R.drawable.psm_alarm_clock, R.drawable.psm_baby_sleeping,R.drawable.psm_bar, R.drawable.psm_barbed_wire2,
            R.drawable.psm_bird3, R.drawable.psm_blue_drop, R.drawable.psm_cat, R.drawable.psm_clutter,
            R.drawable.psm_dog_sleeping, R.drawable.psm_exam4, R.drawable.psm_gambling4, R.drawable.psm_angry_face,
            R.drawable.psm_running4, R.drawable.psm_stressed_person, R.drawable.psm_stressed_person3, R.drawable.psm_stressed_person8
        ),
        intArrayOf(
            R.drawable.psm_alarm_clock2, R.drawable.psm_beach3, R.drawable.psm_headache, R.drawable.psm_hiking3,
            R.drawable.psm_kettle, R.drawable.psm_lake3, R.drawable.psm_lawn_chairs3, R.drawable.psm_lonely,
            R.drawable.psm_lonely2, R.drawable.psm_mountains11, R.drawable.psm_neutral_child, R.drawable.psm_neutral_person2,
            R.drawable.psm_stressed_person6, R.drawable.psm_stressed_person7, R.drawable.psm_to_do_list3, R.drawable.psm_wine3
        ),
        intArrayOf(
            R.drawable.psm_anxious, R.drawable.psm_headache2, R.drawable.psm_peaceful_person, R.drawable.psm_puppy,
            R.drawable.psm_puppy3, R.drawable.psm_reading_in_bed2, R.drawable.psm_running3, R.drawable.psm_sticky_notes2,
            R.drawable.psm_stressed_cat, R.drawable.psm_stressed_person4, R.drawable.psm_stressed_person12, R.drawable.psm_talking_on_phone2,
            R.drawable.psm_to_do_list, R.drawable.psm_work4, R.drawable.psm_yoga4, R.drawable.psm_angry_face
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.img_view)

        val previewImage: ImageView = findViewById(R.id.preview_image)
        val buttonCancel: Button = findViewById(R.id.buttonCancel)
        val buttonSubmit: Button = findViewById(R.id.buttonSubmit)

        val groupIndex = intent.getIntExtra("groupIndex", -1)
        val position = intent.getIntExtra("position", -1)

        if (groupIndex != -1 && position != -1) {
            val imgShow = imageGroups[groupIndex][position]
            previewImage.setImageResource(imgShow)
        }

        buttonCancel.setOnClickListener {
            finish()
        }

        buttonSubmit.setOnClickListener {
            // get the time information
            val currentTime = SimpleDateFormat("yy/MM/dd HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            val stress: Int
            // use the position to get the stress level
            when (position) {
                0 -> stress = 6
                1 -> stress = 8
                2 -> stress = 14
                3 -> stress = 16
                4 -> stress = 5
                5 -> stress = 7
                6 -> stress = 13
                7 -> stress = 15
                8 -> stress = 2
                9 -> stress = 4
                10 -> stress = 10
                11 -> stress = 12
                12 -> stress = 1
                13 -> stress = 3
                14 -> stress = 9
                15 -> stress = 11
                else -> stress = -1
            }
            val newLine = "$currentTime, $stress\n"
            // write data to the csv file (stress.csv)
            val file = File(getExternalFilesDir(null), "stress.csv")
            FileWriter(file, true).use { writer ->
                writer.append(newLine)
            }
            // exit the app
            moveTaskToBack(true)
            finish()
            exitProcess(-1)
        }
    }
}