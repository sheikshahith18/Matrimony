package com.example.matrimony.adapters

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.RequestsListViewBinding
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import kotlin.coroutines.coroutineContext

class ConnectionRequestAdapter(private val connectionsViewModel: ConnectionsViewModel,private val viewFullProfile: (Int) -> Unit) :
    RecyclerView.Adapter<ConnectionRequestAdapter.ConnectionRequestViewHolder>() {

    private var usersList = mutableListOf<UserData>()


    fun setUsersList(list: List<UserData>) {
        Log.i(TAG, "result size ${list.size}")
        this.usersList = list.toMutableList()
        notifyDataSetChanged()
    }


    inner class ConnectionRequestViewHolder(private val binding: RequestsListViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: UserData) {
            binding.tvProfileName.text = user.name
            binding.tvProfileAge.text = user.age.toString()
            binding.tvProfileHeight.text = user.height
            binding.tvProfileEducation.text = "${user.education}/${user.occupation}"
            binding.tvProfileLocation.text = "${user.city}/${user.state}"

            Log.i(TAG,"Connected Request user : $user")
            Glide.with(binding.ivProfilePic.context)
                .load(
                    user.profile_pic ?: ResourcesCompat.getDrawable(
                        binding.ivProfilePic.context.resources,
                        R.drawable.default_profile_pic,
                        null
                    )
                )

                .into(binding.ivProfilePic)

            binding.btnApproveReq.setOnClickListener {
                connectionsViewModel.setConnectionStatus(user.userId, "CONNECTED")
                usersList.remove(user)
                notifyDataSetChanged()

            }

            binding.btnRejectReq.setOnClickListener {
                connectionsViewModel.removeConnection(user.userId)
                usersList.remove(user)
                notifyDataSetChanged()

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConnectionRequestViewHolder {

        val binding: RequestsListViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.requests_list_view,
            parent,
            false
        )
        return ConnectionRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ConnectionRequestViewHolder, position: Int) {
        holder.bind(usersList[position])
        holder.itemView.setOnClickListener {
            viewFullProfile(usersList[position].userId)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }
}