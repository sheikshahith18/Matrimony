package com.example.matrimony.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ConnectedProfilesViewBinding
import com.example.matrimony.models.ConnectionStatus
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.utils.OnDelayClickListener
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.abs


class ConnectedProfilesAdapter(
    private val context: Context,
    private val connectionsViewModel: ConnectionsViewModel,
    private val onCallButtonClick: (userId: Int) -> Unit,
    private val onRemoveButtonClicked: (userId: Int, buttonText: String, adapterPosition: Int) -> Unit,
    private val onScheduleButtonClicked: (userId: Int) -> Unit,
    private val viewFullProfile: (Int) -> Unit

) :
    RecyclerView.Adapter<ConnectedProfilesAdapter.ConnectedProfilesViewHolder>() {


    private var usersList = mutableListOf<UserData>()

    fun setUserList(list: List<UserData>) {
        Log.i(TAG, "connecteduserlistcount ${list.size}")
        this.usersList = list.toMutableList()
        notifyDataSetChanged()
    }


    inner class ConnectedProfilesViewHolder(private val binding: ConnectedProfilesViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val btnText = MutableLiveData<String>("CONNECTED")

        val btnRemoveConnection = binding.btnRemoveConnection

        fun bind(user: UserData) {
            Log.i(TAG, "onBind Connection")
            binding.tvProfileName.text = user.name
            binding.tvProfileAge.text = user.age.toString()
            binding.tvProfileHeight.text = user.height
            binding.tvProfileEducation.text = "${user.education}/${user.occupation}"
            binding.tvProfileLocation.text = "${user.city}/${user.state}"

            binding.imgBtnCall.setOnClickListener {
                onCallButtonClick(user.userId)
            }

            binding.imgBtnSchedule.setOnClickListener {
                onScheduleButtonClicked(user.userId)
            }

            getConnectionStatus(user.userId)

            btnRemoveConnection.text = "Remove Connection"
            btnRemoveConnection.setBackgroundColor(
                context.resources.getColor(
                    R.color.red,
                    null
                )
            )
            binding.btnRemoveConnection.setOnClickListener {

                onRemoveButtonClicked(
                    user.userId,
                    (it as Button).text.toString(),
                    absoluteAdapterPosition
                )

            }

            Log.i(TAG, "Connected Profiles user : $user")
            Glide.with(binding.ivProfilePic.context)
                .load(
                    user.profile_pic ?: ResourcesCompat.getDrawable(
                        binding.ivProfilePic.context.resources,
                        R.drawable.default_profile_pic,
                        null
                    )
                )

                .into(binding.ivProfilePic)
        }

        private fun getConnectionStatus(userId: Int) {

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectedProfilesViewHolder {
        val binding: ConnectedProfilesViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.connected_profiles_view, parent, false
        )

        return ConnectedProfilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConnectedProfilesViewHolder, position: Int) {
        holder.bind(usersList[position])
        holder.itemView.setOnClickListener {

            viewFullProfile(usersList[position].userId)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}