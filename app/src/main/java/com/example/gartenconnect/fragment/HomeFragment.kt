package com.example.gartenconnect.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.gartenconnect.R
import com.example.gartenconnect.ViewModel.ChildViewModel
import com.example.gartenconnect.ViewModel.ClassViewModel
import com.example.gartenconnect.ViewModel.ParentViewModel
import com.example.gartenconnect.ViewModel.RecentUpdateViewModel
import com.example.gartenconnect.activity.MainActivity
import com.example.gartenconnect.adapter.RecentUpdatesAdapter
import com.example.gartenconnect.databinding.FragmentHomeBinding
import com.google.android.material.navigation.NavigationView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ParentViewModel by viewModels()

    private val childViewModel: ChildViewModel by viewModels()

    private val classViewModel: ClassViewModel by viewModels()

    private lateinit var sharePreference: SharedPreferences

    private val adapter = RecentUpdatesAdapter(emptyList())

    private val recentUpdatesViewModel: RecentUpdateViewModel by viewModels()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.show()
        sharePreference = requireContext().getSharedPreferences("GartenConnect", Context.MODE_PRIVATE)
        val navView: NavigationView= requireActivity().findViewById(R.id.nav_view)

        val headerView: View = navView.getHeaderView(0)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
        val navUsername: TextView = headerView.findViewById(R.id.nav_header_textView)
        val navUsePhoto: ImageView = headerView.findViewById(R.id.nav_header_imageView)
        viewModel.getParent()
        viewModel.parentLiveData.observe(viewLifecycleOwner) { parent ->
            navUsername.text = parent?.name
            //aqui va el shared preference
            setNameState(parent?.name.toString())
            Glide.with(this)
                .load(parent?.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(navUsePhoto)
        }
        childViewModel.loadChildrenOfParent()
        childViewModel.childLiveData.observe(viewLifecycleOwner) { child ->

            child?.classId?.let { classViewModel.getClassById(it) }

            binding.childName.text = child?.name
            Glide.with(this)
                .load(child?.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(binding.childProfilePicture)

            classViewModel.classLiveData.observe(viewLifecycleOwner) { class_ ->
                binding.childClass.text = class_?.get(0)?.name
            }

        }
        recentUpdatesViewModel.getRecentUpdates(10)
        binding.recentUpdates.adapter = adapter
        binding.recentUpdates.layoutManager = LinearLayoutManager(requireContext())
        recentUpdatesViewModel.recentUpdateLiveData.observe(viewLifecycleOwner) { recentUpdates ->
            adapter.updateData(recentUpdates)
        }

    }

    private fun setNameState(name: String) {
        val editor = sharePreference.edit()
        editor.putString("username", name)
        editor.apply()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}