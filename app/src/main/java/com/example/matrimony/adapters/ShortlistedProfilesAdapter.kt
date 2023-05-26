package com.example.matrimony.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ShortlistedProfilesListViewBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewImageActivity
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.ui.mainscreen.shortlistsscreen.ShortlistsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShortlistedProfilesAdapter(
    private val context: Context,
    private val userProfileViewModel: UserProfileViewModel,
    private val shortlistsViewModel: ShortlistsViewModel,
    private val settingsViewModel: SettingsViewModel,
    private val albumViewModel: AlbumViewModel,
    private val viewFullProfile: (Int) -> Unit,
    private val noShortlistedProfiles: () -> Unit,
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
            binding.tvProfileAge.text = "👤" + user.age.toString() + ", " + user.height
            binding.tvProfileEducation.text = "🎓" + "${user.education}/${user.occupation}"
            if (user.city != null) {
                if (user.state != null) binding.tvProfileLocation.text =
                    "📍 ${user.city}, ${user.state}"
                else binding.tvProfileLocation.text = "📍 ${user.city}"
                binding.tvProfileLocation.visibility = View.VISIBLE
            } else {
                if (user.state != null) {
                    binding.tvProfileLocation.text = "📍 ${user.state}"
                    binding.tvProfileLocation.visibility = View.VISIBLE
                } else
                    binding.tvProfileLocation.visibility = View.GONE
            }

            CoroutineScope(Dispatchers.Main).launch {

                userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { connectionStatus ->
                        CoroutineScope(Dispatchers.Main).launch {
                            settingsViewModel.getPrivacySettings(user.userId)
                                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) { privacy ->
                                    if (privacy.view_profile_pic == "Everyone" || connectionStatus == "CONNECTED")
                                        if (user.profile_pic != null)
                                            binding.ivProfilePic.setImageBitmap(user.profile_pic)
                                        else
                                            binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)
//                                        Glide.with(binding.ivProfilePic.context)
//                                            .load(
//                                                user.profile_pic ?: R.drawable.default_profile_pic
//                                            )
//                                            .centerCrop()
//                                            .circleCrop()
//                                            .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
//                                            .fitCenter()
//                                            .into(binding.ivProfilePic)
                                    else
//                                        if(user.profile_pic!=null)
//                                            binding.ivProfilePic.setImageBitmap(user.profile_pic)
//                                        else
                                        binding.ivProfilePic.setImageResource(R.drawable.default_profile_pic)
//                                        Glide.with(binding.ivProfilePic.context)
//                                            .load(R.drawable.default_profile_pic)
//                                            .centerCrop()
//                                            .fitCenter()
//                                            .into(binding.ivProfilePic)
                                }

                            albumViewModel.getUserAlbumCount(user.userId)
                                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                                    if (it > 1)
                                        binding.tvAlbumCount.text = it.toString()
                                    else
                                        binding.tvAlbumCount.visibility = View.GONE
                                    if (it > 0) {
                                        binding.ivProfilePic.setOnClickListener {
                                            val intent =
                                                Intent(
                                                    context as MainActivity,
                                                    ViewImageActivity::class.java
                                                )
                                            intent.putExtra("user_id", user.userId)
                                            intent.putExtra("position", 0)
                                            context.startActivity(intent)
                                        }
                                    } else
                                        binding.ivProfilePic.setOnClickListener { view ->
                                            Snackbar.make(
                                                view,
                                                "This user didn't added any images",
                                                Snackbar.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                        }
                    }

            }

            binding.ivProfilePic.setOnClickListener {
                val intent = Intent(context as MainActivity, ViewImageActivity::class.java)
                intent.putExtra("user_id", user.userId)
                intent.putExtra("position", 0)
                context.startActivity(intent)
            }

//            getShortlistStatus(user.userId)
            binding.imgBtnShortlist.setImageResource(R.drawable.ic_baseline_favorite_enabled)


            binding.imgBtnShortlist.setOnClickListener {
                if (bindingAdapterPosition < 0)
                    return@setOnClickListener
//                    shortlistsViewModel.removeFromShortLists.add(user.userId)
//                usersList.removeAt(bindingAdapterPosition)
//                notifyItemRemoved(bindingAdapterPosition)
                shortlistsViewModel.removeShortlist(user.userId)
//                if (isShortlisted) {
                if (usersList.isEmpty()) {
                    noShortlistedProfiles()
                }
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
//        if(position<0)return
        holder.bind(usersList[position])
        holder.itemView.setOnClickListener {
            viewFullProfile(usersList[position].userId)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}