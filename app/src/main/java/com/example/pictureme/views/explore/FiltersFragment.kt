package com.example.pictureme.views.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pictureme.data.models.Filter
import com.example.pictureme.data.models.Picme
import com.example.pictureme.databinding.FragmentFiltersBinding
import com.example.pictureme.utils.FilterPicmes.filterFriendsPicmes
import com.example.pictureme.utils.FilterPicmes.filterNewestPicmes
import com.example.pictureme.utils.FilterPicmes.filterOldestPicmes
import com.example.pictureme.viewmodels.FilteredPicmesViewModel
import com.example.pictureme.viewmodels.PicmeViewModel
import com.example.pictureme.viewmodels.UserViewModel
import com.example.pictureme.views.explore.adapters.FilterAdapter

class FiltersFragment : Fragment() {
    private var _binding: FragmentFiltersBinding? = null
    private val binding get() = _binding!!
    private val filteredPicmesViewModel by activityViewModels<FilteredPicmesViewModel>()
    private val picmeViewModel by activityViewModels<PicmeViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private var filter = Filter()
    private lateinit var adapter: FilterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FilterAdapter(emptyList(), filter)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFiltersBinding.inflate(inflater, container, false)

        binding.rcFilterFriends.adapter = adapter
        binding.rcFilterFriends.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        userViewModel.userLiveData.observe(viewLifecycleOwner) { user ->
            val friendships = user.friendships!!

            // If the user has no friends, don't display friends filter
            if (friendships.isNotEmpty()) {
                adapter.setList(friendships)
                binding.cvFriends.visibility = View.VISIBLE
            }

        }

        setFilter()
        setGoBackButton()
        selectFilter(adapter)

        return (binding.root)
    }

    private fun setGoBackButton() {
        binding.buttonGoBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setFilter() {
        filteredPicmesViewModel.filterLiveData.observe(viewLifecycleOwner) { filter ->
            if (filter.sortBy != null) {
                val chosenBtn = requireActivity().findViewById<RadioButton>(filter.sortBy!!)
                chosenBtn.isChecked = true
            }
            adapter.filter = filter
            this.filter = filter
        }
    }

    private fun selectFilter(adapter: FilterAdapter) {
        var filteredPicmes: List<Picme>
        binding.buttonSave.setOnClickListener {
            val checkedRBSortBy = binding.sortByRB.checkedRadioButtonId
            filter.sortBy = checkedRBSortBy
            filteredPicmesViewModel.updateFilter(filter)
            picmeViewModel.picmesLiveData.observe(viewLifecycleOwner) { response ->
                filteredPicmesViewModel.addPicmeList(response)
                filteredPicmes = emptyList()
                if (checkedRBSortBy == binding.newestRB.id) {
                    filteredPicmes = filterNewestPicmes(response)
                } else if (checkedRBSortBy == binding.oldestRB.id) {
                    filteredPicmes = filterOldestPicmes(response)
                } else {
                    filteredPicmes = response
                }
                // Sort By & Selected friends
                if (filter.friendIds.isNotEmpty()) {
                    filteredPicmes = filterFriendsPicmes(filteredPicmes, filter.friendIds)
                }
                filteredPicmesViewModel.addPicmeList(filteredPicmes)
            }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}