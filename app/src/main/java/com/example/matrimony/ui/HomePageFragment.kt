package com.example.matrimony.ui

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.example.matrimony.R
import com.example.matrimony.adapters.ProfilesGridAdapter
import com.example.matrimony.databinding.FragmentHomePageBinding
import com.example.matrimony.models.UserData


class HomePageFragment : Fragment() {

    lateinit var binding: FragmentHomePageBinding

    lateinit var navDrawer: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_page, container, false)
        setUpNavigationDrawer()
        initPreferredProfiles()
        initOtherProfiles()
        return binding.root
    }

    private fun setUpNavigationDrawer() {
        navDrawer = binding.drawerLayout
//        actionBarDrawerToggle =
//            ActionBarDrawerToggle(requireActivity(), navDrawer, R.string.open, R.string.close)

//        navDrawer.addDrawerListener(actionBarDrawerToggle)
//        actionBarDrawerToggle.syncState()
//        navDrawer.fitsSystemWindows=true

        binding.ivProfilePic.setOnClickListener {
            navDrawer.openDrawer(GravityCompat.START)
        }
    }

    private fun initPreferredProfiles(){
        val userList= mutableListOf(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.kakashi_img)))


        val profilesRecyclerView=binding.rvPreferredProfiles

        profilesRecyclerView.layoutManager=GridLayoutManager(requireContext(),1,GridLayoutManager.HORIZONTAL,false)

        val profilesGridAdapter=ProfilesGridAdapter(userList)
        profilesRecyclerView.adapter=profilesGridAdapter

        profilesGridAdapter.notifyDataSetChanged()
    }

    fun initOtherProfiles(){
        val userList= mutableListOf(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))
        userList.add(UserData("name",21,BitmapFactory.decodeResource(resources,R.drawable.itachi_img)))


        val profilesRecyclerView=binding.rvOtherProfiles

        profilesRecyclerView.layoutManager=GridLayoutManager(requireContext(),1,GridLayoutManager.HORIZONTAL,false)

        val profilesGridAdapter=ProfilesGridAdapter(userList)
        profilesRecyclerView.adapter=profilesGridAdapter

        profilesGridAdapter.notifyDataSetChanged()
    }

}


///   63618902841