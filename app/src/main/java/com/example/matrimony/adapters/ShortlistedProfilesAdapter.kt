package com.example.matrimony.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ShortlistedProfilesListViewBinding
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.ui.mainscreen.shortlistsscreen.ShortlistsViewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShortlistedProfilesAdapter(
    private val userProfileViewModel: UserProfileViewModel,
    private val shortlistsViewModel: ShortlistsViewModel,
    private val settingsViewModel: SettingsViewModel,
    private val viewFullProfile: (Int) -> Unit
) :
    RecyclerView.Adapter<ShortlistedProfilesAdapter.ShortlistedProfilesViewHolder>() {

    var usersList = mutableListOf<UserData>()

    fun setUserList(list: List<UserData>) {
        Log.i(TAG, "ShortlistAdapter listSize:${list.size}")
        Log.i(TAG, "ShortlistAdapter userListSize:${usersList.size}")
        usersList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class ShortlistedProfilesViewHolder(private val binding: ShortlistedProfilesListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isShortlisted = true

        fun bind(user: UserData) {
            binding.tvProfileName.text = user.name
            binding.tvProfileAge.text = user.age.toString() + ", " + user.height
            binding.tvProfileEducation.text = "${user.education}/${user.occupation}"
            binding.tvProfileLocation.text = "${user.city}/${user.state}"

            CoroutineScope(Dispatchers.Main).launch {

                userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { connectionStatus ->
                        CoroutineScope(Dispatchers.Main).launch {
                            settingsViewModel.getPrivacySettings(user.userId)
                                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { privacy ->
                                    if (privacy.view_profile_pic == "Everyone" || connectionStatus == "CONNECTED")
                                        Glide.with(binding.ivProfilePic.context)
                                            .load(
                                                user.profile_pic ?: R.drawable.default_profile_pic
                                            )
                                            .centerCrop()
                                            .circleCrop()
                                            .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                                            .fitCenter()
                                            .into(binding.ivProfilePic)
                                    else
                                        Glide.with(binding.ivProfilePic.context)
                                            .load(R.drawable.default_profile_pic)
                                            .centerCrop()
                                            .fitCenter()
                                            .into(binding.ivProfilePic)
                                }
                        }
                    }
            }

            getShortlistStatus(user.userId)

            binding.imgBtnShortlist.setOnClickListener {

//                if (isShortlisted) {
//                    shortlistsViewModel.removeFromShortLists.add(user.userId)
//                    isShortlisted = false
//                    (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_hollow)
//                } else {
//                    shortlistsViewModel.removeFromShortLists.remove(user.userId)
//                    isShortlisted = true
//                    (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_enabled)
//                }
            }


        }

        private fun getShortlistStatus(userId: Int) {
            CoroutineScope(Dispatchers.Main).launch {
                shortlistsViewModel.getShortlistedStatus(userId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {

                        Log.i(TAG, "userId=$userId ShortlistPage Shortlist status Observer")
                        isShortlisted = it
                        if (isShortlisted) {
                            Log.i(TAG, "$userId saved already")
                            binding.imgBtnShortlist.setImageResource(R.drawable.ic_baseline_favorite_enabled)
//                            binding.imgBtnShortlist.tooltipText = "shortlisted"
                        } else {
                            binding.imgBtnShortlist.setImageResource(R.drawable.ic_baseline_favorite_hollow)
//                            binding.imgBtnShortlist.tooltipText = "shortlist"
                        }
                    }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShortlistedProfilesViewHolder {
        val binding: ShortlistedProfilesListViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.shortlisted_profiles_list_view,
            parent,
            false
        )
        return ShortlistedProfilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShortlistedProfilesViewHolder, position: Int) {
        holder.bind(usersList[position])
        holder.itemView.setOnClickListener {
            viewFullProfile(usersList[position].userId)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

}