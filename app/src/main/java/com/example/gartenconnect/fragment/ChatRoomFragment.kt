package com.example.gartenconnect.fragment

import android.app.AlertDialog
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
import com.example.gartenconnect.R
import com.example.gartenconnect.ViewModel.ChatRoomViewModel
import com.example.gartenconnect.ViewModel.TeacherViewModel
import com.example.gartenconnect.adapter.ChatRoomAdapter
import com.example.gartenconnect.databinding.FragmentChatRoomBinding
import com.example.gartenconnect.model.ChatRoom
import com.google.firebase.auth.FirebaseAuth

class ChatRoomFragment : Fragment() {

    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TeacherViewModel by viewModels()
    private val chatRoomViewModel: ChatRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chatRoomAdapter = ChatRoomAdapter(emptyList(), viewLifecycleOwner, viewModel)
        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatListRecyclerView.adapter = chatRoomAdapter
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack(R.id.homeFragment, false)
            }
        })
        val builder = AlertDialog.Builder(requireContext())

        viewModel.teacherLiveData.observe(viewLifecycleOwner) { teachers ->
            val teacherNames = teachers.map { it.name }.toTypedArray()

            builder.setTitle("Elige un profesor para contactar")
            builder.setItems(teacherNames) { dialog, which ->
                val selectedTeacher = teachers[which]
                Log.i("ChatRoomFragment", "Seleccionaste a $selectedTeacher")

                chatRoomViewModel.chatRoomLiveData.value?.let { chatRooms ->
                    if (chatRooms.none { it.user2Id == selectedTeacher.id }) {
                        val chatRoom = ChatRoom(user1Id = FirebaseAuth.getInstance().currentUser?.uid, user2Id = selectedTeacher.id)
                        chatRoomViewModel.saveChatRoom(chatRoom, {
                            val action = ChatRoomFragmentDirections.actionChatFragmentToMessageFragment(chatRoom)
                            findNavController().navigate(action)
                        }, { error ->
                            Log.e("ChatRoomFragment", "Error al guardar la sala de chat: ${error.message}")
                        })
                    } else {
                        Log.i("ChatRoomFragment", "Ya existe una sala de chat con este profesor")
                    }
                }
            }

            builder.setNegativeButton("Cancelar") { dialog, _ ->
                dialog.dismiss()
            }
            binding.addChatFab.setOnClickListener {
                builder.show()
            }
        }

        chatRoomViewModel.chatRoomLiveData.observe(viewLifecycleOwner) { chatRooms ->
            chatRoomAdapter.updateData(chatRooms)
        }

        chatRoomViewModel.getChatRoom()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
