package com.example.gartenconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.gartenconnect.ViewModel.TeacherViewModel
import com.example.gartenconnect.databinding.FragmentBlogDetailBinding
import com.example.gartenconnect.model.BlogPost
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
class BlogDetailFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentBlogDetailBinding? = null
    val binding get() = _binding!!
    private lateinit var post: BlogPost
    private val viewModel: TeacherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        post = arguments?.getParcelable("blogPost")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBlogDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheetDialog = dialog as BottomSheetDialog
        bottomSheetDialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED

        val timestamp = post.date
        val milliseconds = timestamp!!.seconds * 1000 + timestamp.nanoseconds / 1000000
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.blogDetailTitle.text = post.title
        binding.blogDetailContent.text = post.content
        binding.blogDetailDate.text = sdf.format(Date(milliseconds))
        post.authorId?.let {
            viewModel.getTeacherById(it).observe(viewLifecycleOwner) { teacher ->
                binding.blogDetailAuthor.text = teacher?.name
            }
        }

        Glide.with(binding.root)
            .load(post.photoUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(binding.blogDetailImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
