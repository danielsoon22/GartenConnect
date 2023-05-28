package com.example.gartenconnect.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gartenconnect.ViewModel.ChatViewModel
import com.example.gartenconnect.ViewModel.TeacherViewModel
import com.example.gartenconnect.adapter.ChatAdapter
import com.example.gartenconnect.databinding.FragmentMessageBinding
import com.example.gartenconnect.model.ChatRoom
import com.google.firebase.auth.FirebaseAuth

class MessageFragment : Fragment() {

    private var _binding: FragmentMessageBinding? = null
    private val binding get() = _binding!!
    private lateinit var chatRoom: ChatRoom
    private val viewModel: ChatViewModel by viewModels()
    private val teacherViewModel: TeacherViewModel by viewModels()
    private var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatRoom = arguments?.getParcelable("chatRoom")!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getChats(chatRoom.id!!)
        val adapter = ChatAdapter(emptyList(), parentFragmentManager, viewLifecycleOwner, teacherViewModel)
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(context)
        viewModel.chatLiveData.observe(viewLifecycleOwner) {chats->
            adapter.updateData(chats)
        }
        binding.sendButton.setOnClickListener {
            viewModel.createMessage(chatRoom.id!!, FirebaseAuth.getInstance().currentUser!!.uid, chatRoom.user2Id!!, binding.messageInput.text.toString())
            binding.messageInput.text.clear()
            viewModel.getChats(chatRoom.id!!)
        }

        // Deshabilitar el botón de retroceso durante el fragmento de mensajes
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
    }

    override fun onResume() {
        super.onResume()
        // Habilitar el botón de retroceso en el ActionBar
        onBackPressedCallback?.isEnabled = true
    }

    override fun onPause() {
        super.onPause()
        // Deshabilitar el botón de retroceso en el ActionBar
        onBackPressedCallback?.isEnabled = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Remover el callback del botón de retroceso
        onBackPressedCallback?.remove()
    }
}
