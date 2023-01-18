package com.example.pictureme.views.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.data.models.Friendship
import com.example.pictureme.databinding.FragmentFiltersBinding
import com.example.pictureme.utils.FilterPicmes.filterFoodPicmes
import com.example.pictureme.utils.FilterPicmes.filterNewestPicmes
import com.example.pictureme.utils.FilterPicmes.filterOldestPicmes
import com.example.pictureme.viewmodels.FilteredPicmesViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import com.example.pictureme.views.addfriends.adapters.AddFriendsAdapter
import com.example.pictureme.views.explore.adapters.FilterAdapter

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!
    private val filteredPicmesViewModel by activityViewModels<FilteredPicmesViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)
        println("CREATEVIEWFILTERSSS")
        setGoBackButton()
        selectFilter()
        val adapter = FilterAdapter(emptyList())
        binding.rcFilterFriends.adapter = adapter
        binding.rcFilterFriends.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            val friendships = user.friendships!!
            adapter.setList(friendships)


        }
        return (binding.root)
    }

    private fun setGoBackButton() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun selectFilter() {
        binding.buttonSave.setOnClickListener {
            val checkedRBSortBy = binding.sortByRB.checkedRadioButtonId
            picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
                if (checkedRBSortBy == binding.newestRB.id) {
                    val newestPicmes = filterNewestPicmes(response)
                    println(newestPicmes)
                    filteredPicmesViewModel.addPicmeList(newestPicmes)
                }
                else if (checkedRBSortBy == binding.latestRB.id) {
                    val latestPicmes = filterOldestPicmes(response)
                    println(latestPicmes)
                    filteredPicmesViewModel.addPicmeList(latestPicmes)
                }
            }
            filteredPicmesViewModel.filteredPicmesLiveData.observe(viewLifecycleOwner){ response ->
                println(response)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}