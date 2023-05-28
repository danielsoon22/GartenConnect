package com.example.gartenconnect.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gartenconnect.R
import com.example.gartenconnect.ViewModel.AttendanceViewModel
import com.example.gartenconnect.ViewModel.ChildViewModel
import com.example.gartenconnect.ViewModel.ClassViewModel
import com.example.gartenconnect.adapter.AtterndanceStudentAdapter
import com.example.gartenconnect.databinding.FragmentStudentDetailBinding

class StudentDetailFragment : Fragment() {
    private var _binding: FragmentStudentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChildViewModel by viewModels()
    private val attendanceViewModel: AttendanceViewModel by viewModels()
    val adapter = AtterndanceStudentAdapter(emptyList())
    private val classViewModel: ClassViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
        viewModel.loadChildrenOfParent()
        attendanceViewModel.getAttendanceOfChild()
        viewModel.childLiveData.observe(viewLifecycleOwner) { child->
            child?.classId?.let { classViewModel.getClassById(it) }
            Glide.with(this)
                .load(child?.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.studentImage)
            binding.studentName.text = child?.name
            binding.studentAge.text = child?.age.toString() + " years old"
            classViewModel.classLiveData.observe(viewLifecycleOwner) { class_ ->
                binding.studentGrade.text = class_?.get(0)?.name
            }
        }

        // Create the adapter

        binding.studentAttendanceRV.layoutManager = LinearLayoutManager(context)
        binding.studentAttendanceRV.adapter = adapter
// Observe the data
        attendanceViewModel.attendanceLiveData.observe(viewLifecycleOwner) { attendance ->
            // Update the adapter's data
            Log.i("Attendance", attendance.toString())
            adapter.updateData(attendance.filterNotNull())
        }
    }
}
