package com.example.matrimony.adapters

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ProfilesHorizontalGridViewBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs

class ProfilesGridAdapter(
    private val viewModel: UserProfileViewModel,
    private val settingsViewModel: SettingsViewModel,
    private val viewFullProfile: (Int) -> Unit
) :
    RecyclerView.Adapter<ProfilesGridAdapter.ProfilesGridViewHolder>() {

    private var userList: List<UserData> = mutableListOf()

    fun setList(list: List<UserData>) {
        this.userList = list.toMutableList()
        notifyDataSetChanged()
    }

    inner class ProfilesGridViewHolder(private val binding: ProfilesHorizontalGridViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val profileDp = binding.ivProfilePic
        private val profileName = binding.tvProfileName
        private val profileAge = binding.tvProfileAge

        fun bind(user: UserData) {
            profileName.text = userList[absoluteAdapterPosition].name
            profileAge.text = userList[absoluteAdapterPosition].age.toString()

            CoroutineScope(Dispatchers.Main).launch {

                viewModel.getConnectionStatus(viewModel.currentUserId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { connectionStatus ->
                        CoroutineScope(Dispatchers.Main).launch {
                            settingsViewModel.getPrivacySettings(user.userId)
                                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { privacy ->


                                    if (privacy.view_profile_pic == "Everyone" || connectionStatus == "CONNECTED")
                                        Glide.with(profileDp.context)
                                            .load(
                                                user.profile_pic ?: R.drawable.default_profile_pic
                                            )
                                            .centerCrop()
                                            .fitCenter()
                                            .into(profileDp)
                                    else
                                        Glide.with(profileDp.context)
                                            .load(R.drawable.default_profile_pic)
                                            .centerCrop()
                                            .fitCenter()
                                            .into(profileDp)

                                }
                        }
                    }
            }


            binding.ivProfilePic.setOnClickListener {
                viewFullProfile(user.userId)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilesGridViewHolder {
        val binding: ProfilesHorizontalGridViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.profiles_horizontal_grid_view, parent, false
        )

        return ProfilesGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfilesGridViewHolder, position: Int) {
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}