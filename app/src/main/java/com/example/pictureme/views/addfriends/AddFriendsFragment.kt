package com.example.pictureme.views.addfriends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.R
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.databinding.FragmentAddFriendsBinding
import com.example.pictureme.viewmodels.PreviewPicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import com.example.pictureme.views.addfriends.adapters.AddFriendsAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddFriendsFragment : Fragment() {

    private var _binding: FragmentAddFriendsBinding? = null
    private val binding get() = _binding!!

    private val userViewModel by activityViewModels<UserViewModel>()
    private val previewViewModel by activityViewModels<PreviewPicmeViewModel>()

    private var friendships: List<Friendship> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddFriendsBinding.inflate(inflater, container, false);

        binding.searchView.clearFocus()

        setupListeners()

        val adapter = AddFriendsAdapter(friendships, previewViewModel)
        binding.rvFriends.adapter = adapter
        binding.rvFriends.layoutManager = LinearLayoutManager(activity)

        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            val friendships = user.friendships!!
            adapter.setList(friendships)

            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    binding.searchView.clearFocus()
                    return false;
                }

                override fun onQueryTextChange(p0: String?): Boolean {
                    if (p0 == null) return true

                    val filteredList = ArrayList<Friendship>()
                    for (friendship in friendships) {
                        if (friendship.friend!!.fullName!!.lowercase().contains(p0.lowercase())) {
                            filteredList.add(friendship)
                        }
                    }

                    adapter.setList(filteredList)
                    return true
                }

            })

        }

        return (binding.root)
    }

    private fun setupListeners() {

        binding.buttonGoBack.setOnClickListener{
            Navigation.findNavController(binding.root)
                .popBackStack()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}