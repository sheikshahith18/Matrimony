package com.example.matrimony.adapters

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matrimony.R
import com.example.matrimony.databinding.ProfilesHorizontalGridViewBinding
import com.example.matrimony.models.UserData

class ProfilesGridAdapter(private val userList: MutableList<UserData>) :
    RecyclerView.Adapter<ProfilesGridAdapter.ProfilesGridViewHolder>() {

    inner class ProfilesGridViewHolder(private val binding: ProfilesHorizontalGridViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val profileDp = binding.ivProfilePic
        val profileName = binding.tvProfileName
        val profileAge = binding.tvProfileAge
    }


    fun addUser(user: UserData) {
        userList.add(user)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfilesGridViewHolder {
        val binding: ProfilesHorizontalGridViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.profiles_horizontal_grid_view, parent, false
        )

        return ProfilesGridViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProfilesGridViewHolder, position: Int) {
        holder.profileName.text = userList[position].name
        holder.profileAge.text = userList[position].age.toString()
        Glide.with(holder.profileDp.context)
            .load(userList[position].profilePic as Bitmap)
            .centerCrop()
            .fitCenter()
            .into(holder.profileDp)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}


//class CustomAdapter(
//    private val myList: List<ItemsViewModel>,
//    private val context: Context
//) :
//    RecyclerView.Adapter<CustomAdapter.ViewHolder>() {
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val imageView: ImageView = this.itemView.findViewById(R.id.imageview)
//        val textView: TextView = this.itemView.findViewById(R.id.textView)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
////        super.onCreateViewHolder
//        val view =
//            LayoutInflater.from(parent.context).inflate(R.layout.card_view_design, parent, false)
//        Log.i("info", "OnCreateViewHolder")
//        return ViewHolder(view)
//    }
//
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val itemsViewModel = myList[position]
//        holder.imageView.setImageResource(itemsViewModel.image)
//        holder.textView.text = itemsViewModel.text
//        Log.i("info", "OnBindViewHolder")
//        holder.itemView.setOnClickListener {
//            Toast.makeText(context, "item selected -> ${holder.textView.text}", Toast.LENGTH_SHORT)
//                .show()
//        }
//    }
//
//    override fun getItemCount(): Int {
//        return myList.size
//    }
//
//
//}