package com.example.matrimony.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ProfilesSearchResultsViewBinding
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.models.ConnectionStatus
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchPageAdapter(
    private val context: Context,
    private val userProfileViewModel: UserProfileViewModel,
    private val connectionsViewModel: ConnectionsViewModel,
    private val settingsViewModel: SettingsViewModel,
    private val onConnectionButtonClicked: (Int, String, Int, String) -> Unit,
    private val viewFullProfile: (Int) -> Unit
) :
    RecyclerView.Adapter<SearchPageAdapter.ProfilesViewHolder>() {

    private var userList = mutableListOf<UserData>()

    fun setList(list: List<UserData>) {
        Log.i(TAG, "listSize= ${list.size}")
        this.userList = list.toMutableList()
        connectionsViewModel.removeConnection1.forEach {
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeConnection1.clear()
        connectionsViewModel.sendConnectionsTo.forEach {
            connectionsViewModel.addConnection(
                Connections(
                    user_id = connectionsViewModel.userId,
                    connected_user_id = it,
                    status = "REQUESTED"
                )
            )
        }
        connectionsViewModel.sendConnectionsTo.clear()
        connectionsViewModel.removeFromConnections.forEach {
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeFromConnections.clear()

        notifyDataSetChanged()
    }


    inner class ProfilesViewHolder(private val binding: ProfilesSearchResultsViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val profileName = binding.tvProfileName

        private var isShortlisted = false
        var connectionStatus: ConnectionStatus? = ConnectionStatus.NOT_CONNECTED
        val btnConnection = binding.imgBtnSendReq

        fun bind(user: UserData) {
            CoroutineScope(Dispatchers.Main).launch {

                userProfileViewModel.getConnectionStatus(user.userId)
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

            binding.tvProfileName.text = user.name

            binding.tvAgeHeight.text = "${user.age}"
            if (user.height != null)
                binding.tvAgeHeight.text = "👤 ${user.age}, ${user.height}"

            if (user.education != null) {
                binding.tvEducation.text = "🎓 ${user.education}"
                binding.tvEducation.visibility = View.VISIBLE
            } else
                binding.tvEducation.visibility = View.GONE

            if (user.occupation != null) {
                binding.tvOccupation.text = "💼 ${user.occupation}"
                binding.tvOccupation.visibility = View.VISIBLE
            } else
                binding.tvOccupation.visibility = View.GONE


            if (user.city != null) {
                if (user.state != null) binding.tvLocation.text = "📍 ${user.city}, ${user.state}"
                else binding.tvLocation.text = "📍 ${user.city}"
                binding.tvLocation.visibility = View.VISIBLE
            } else {
                if (user.state != null) {
                    binding.tvLocation.text = "📍 ${user.state}"
                    binding.tvLocation.visibility = View.VISIBLE
                } else
                    binding.tvLocation.visibility = View.GONE
            }

            getShortlistStatus(user.userId, user.name)

            getConnectionStatus(user.userId, user.name)

        }

        private fun getShortlistStatus(userId: Int, name: String) {
            CoroutineScope(Dispatchers.Main).launch {
                userProfileViewModel.getShortlistedStatus(userId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
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

            binding.imgBtnShortlist.setOnClickListener {
                Log.i(
                    TAG,
                    "current user id = ${userProfileViewModel.userId}, shortlisted user id = $userId"
                )
                if (isShortlisted) {
                    isShortlisted = false
                    Log.i(TAG, "removed")
                    userProfileViewModel.removeShortlist(userProfileViewModel.userId, userId)
                    (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_hollow)
//                    it.tooltipText = "shortlist"
                    Toast.makeText(context, "Shortlist Removed For $name", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    isShortlisted = true
                    Log.i(TAG, "shortlisted")
                    userProfileViewModel.shortlistUser(
                        Shortlists(
                            0,
                            userProfileViewModel.userId,
                            userId
                        )
                    )
                    (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_enabled)
//                    it.tooltipText = "shortlisted"
                    Toast.makeText(context, "Shortlisted $name", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun getConnectionStatus(userId: Int, name: String) {
            CoroutineScope(Dispatchers.Main).launch {
                connectionsViewModel.getConnectionDetails(connectionsViewModel.userId, userId)
                    .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {
                        Log.i(TAG, "Search Adapter CoroutineScope Connection")
                        Log.i(TAG, "Adapter Position : $absoluteAdapterPosition")
                        Log.i(TAG, "Connection Status : ${it?.status}")
                        if (it == null) {
                            connectionStatus = ConnectionStatus.NOT_CONNECTED
                            binding.imgBtnSendReq.setImageResource(R.drawable.ic_send_connection)
                            return@observe
                        }
                        connectionStatus = when (it.status) {
                            ConnectionStatus.CONNECTED.toString() -> {
                                binding.imgBtnSendReq.setImageResource(R.drawable.ic_remove_connection)
                                ConnectionStatus.CONNECTED
                            }
                            ConnectionStatus.REQUESTED.toString() -> {
                                val value: ConnectionStatus? =
                                    if (connectionsViewModel.userId == it.user_id) {
                                        binding.imgBtnSendReq.setImageResource(R.drawable.ic_connection_sent)
                                        ConnectionStatus.REQUESTED
                                    } else {
                                        null
                                    }

                                value
                            }
                            else -> {
                                binding.imgBtnSendReq.setImageResource(R.drawable.ic_send_connection)
                                //                                    binding.imgBtnSendReq.tooltipText = "not_connected"
                                ConnectionStatus.NOT_CONNECTED
                            }
                        }
                    }

                btnConnection.setOnClickListener {
                    Log.i(TAG, "connectionStatus $connectionStatus")
                    onConnectionButtonClicked(
                        userId,
                        connectionStatus.toString(),
                        bindingAdapterPosition,
                        name
                    )
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilesViewHolder {
        val binding: ProfilesSearchResultsViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.profiles_search_results_view, parent, false
        )
        return ProfilesViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ProfilesViewHolder, position: Int) {
        holder.profileName.text = userList[position].name

        holder.itemView.setOnClickListener {
            viewFullProfile(userList[position].userId)
        }

        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

}
