package com.example.qiuhao_zheng_stressmeter.ui.home

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.ViewModelProvider
import com.example.qiuhao_zheng_stressmeter.R
import com.example.qiuhao_zheng_stressmeter.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var gridAdapter: GridViewAdapter
//    private lateinit var moreImgsButton: Button
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    private var isFirstTime = true

    // three groups of images, each group has 16 images
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

    private var groupIndex = 0 // current group index, start from 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        // communication bewtten fragments
        setFragmentResultListener("updateIsFirstTimeKey") { _, bundle ->
            val newValue = bundle.getBoolean("isFirstTime")
            println("qz: $newValue")
            isFirstTime = newValue
        }

        gridViewImages()
        // Set up the "MORE IMAGES" button
        binding.moreImages.setOnClickListener {
            isFirstTime = false
            stopSound()
            vibrator.cancel()
//            if (vibrator.hasVibrator()) {
//                vibrator.cancel()
//            }
//            if (vibrator.hasVibrator()) {
//                println("qz: yes")
//            }
//            if (!vibrator.hasVibrator()) {
//                println("qz: no")
//            }
            groupIndex = (groupIndex + 1) % imageGroups.size
            gridAdapter.notifyDataSetChanged()
        }


        mediaPlayer = MediaPlayer.create(context, R.raw.startsound)
        if (isFirstTime) {
            mediaPlayer.isLooping = true
            // start playing the sound
            mediaPlayer.start()
//            startPlayback()
        }

        vibrator = requireContext().getSystemService(android.content.Context.VIBRATOR_SERVICE) as Vibrator
        if (isFirstTime) {
            // start vibration
            vibrator.vibrate(longArrayOf(0, 1000), 0)
        }
//        if (vibrator.hasVibrator()) {
//            println("qz: vib")
//        }
//        if (!vibrator.hasVibrator()) {
//            println("qz: no")
//        }
        setFragmentResultListener("updateIsFirstTimeKey") { _, bundle ->
            val newValue = bundle.getBoolean("isFirstTime")
//            println("qz: $newValue")
            isFirstTime = newValue
            stopSound()
            vibrator.cancel()
        }

        return root
    }

    // stop playing the sound
    private fun stopSound() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.stop()
        }
    }
    private fun gridViewImages() {
        val gridView = binding.gridViewImgs
        gridAdapter = GridViewAdapter()
        gridView.adapter = gridAdapter
        gridView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // image click
//            println("qz: $position") // test use only
            isFirstTime = false
            // stop playing the sound
            stopSound()
            // cancel the vibration
            vibrator.cancel()
//            if (vibrator.hasVibrator()) {
//                vibrator.cancel()
//            }
//            if (vibrator.hasVibrator()) {
//                println("qz: yes")
//            }
//            if (!vibrator.hasVibrator()) {
//                println("qz: no")
//            }
            val intent = Intent(requireContext(), ImgView::class.java).apply {
                putExtra("groupIndex", groupIndex)
                putExtra("position", position)
            }
            startActivity(intent)
        }
    }

    override fun onPause() {
        super.onPause()
//        isFirstTime = false
//        mediaPlayer.release()
//        vibrator.cancel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mediaPlayer.release()
        vibrator.cancel()
    }

    private inner class GridViewAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return imageGroups[groupIndex].size
        }

        override fun getItem(position: Int): Any {
            return imageGroups[groupIndex][position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val imageView = ImageView(context).apply {
                layoutParams = ViewGroup.LayoutParams(250, 250)
            }
            imageView.setImageResource(imageGroups[groupIndex][position])
            return imageView
        }
    }
}