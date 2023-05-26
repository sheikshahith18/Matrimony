package com.example.matrimony.ui.mainscreen.shortlistsscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ShortlistedProfilesAdapter
import com.example.matrimony.databinding.FragmentShortlistsPageBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.utils.CURRENT_USER_GENDER
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ShortlistsPageFragment : Fragment() {

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val shortlistsViewModel by activityViewModels<ShortlistsViewModel>()
    private val settingsViewModel by activityViewModels<SettingsViewModel>()
    private val albumViewModel by activityViewModels<AlbumViewModel>()

    lateinit var binding: FragmentShortlistsPageBinding

    private var fragmentView: View? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "shortlistFrag onCreate")
        if (fragmentView == null) {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_shortlists_page,
                container,
                false
            )
            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
//            userProfileViewModel.userId = userId
            shortlistsViewModel.userId = userId
            shortlistsViewModel.gender =
                sharedPref.getString(CURRENT_USER_GENDER, null) ?: throw java.lang.Exception("")
            fragmentView = binding.root
            loadShortlistedProfiles()
        }
        Log.i(TAG, "ShortlistPage currUserId: ${shortlistsViewModel.userId}")
        return fragmentView
    }

    override fun onPause() {
        super.onPause()
        shortlistsViewModel.removeFromShortLists.forEach { shortlistedUserId ->
            shortlistsViewModel.removeShortlist(shortlistedUserId)
        }
        shortlistsViewModel.removeFromShortLists.clear()
    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun loadShortlistedProfiles() {

        Log.i(TAG, "Load shortlist profiles")

        val shortlistedProfilesView = binding.rvShortlists
        shortlistedProfilesView.layoutManager =
            LinearLayoutManager(requireContext())
//            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        val adapter = ShortlistedProfilesAdapter(
            requireActivity(),
            userProfileViewModel,
            shortlistsViewModel,
            settingsViewModel,
            albumViewModel,
            viewFullProfile,
            noShortlistedProfiles
        )
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        shortlistedProfilesView.adapter = adapter
//        val shortListedProfiles =
//            userProfileViewModel.getShortlistedProfiles(userProfileViewModel.userId)

        lifecycleScope.launch {
            shortlistsViewModel.getShortlistedProfiles(shortlistsViewModel.userId)
                .observe(requireActivity()) { shortlistedUserList ->
                    Log.i(TAG, "shortlist size=${shortlistedUserList.size}")
                    if (shortlistedUserList.isEmpty()) {
                        binding.rvShortlists.visibility = View.GONE
                        binding.noProfilesMessage.visibility = View.VISIBLE
                        return@observe
                    }
                    binding.rvShortlists.visibility = View.VISIBLE
                    binding.noProfilesMessage.visibility = View.GONE

                    val usersList = mutableListOf<UserData>()

                    lifecycleScope.launch {
                        shortlistedUserList.forEach { userId ->
                            val userData = shortlistsViewModel.getUserData(userId)
                            Log.i(TAG, userData.toString())
                            usersList.add(userData)
                        }
                        adapter.setUserList(usersList)
                    }

                }

        }


    }

    private val noShortlistedProfiles={
        binding.rvShortlists.visibility = View.GONE
        binding.noProfilesMessage.visibility = View.VISIBLE
    }


}

