package com.example.matrimony.ui.mainscreen.homescreen.profilescreen

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ImageSwipeAdapter
import com.example.matrimony.adapters.ViewPagerAdapter
import com.example.matrimony.databinding.PageViewProfileBinding
import com.example.matrimony.db.entities.Album
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.models.ConnectionStatus
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.ConnectionsViewModel
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionDialogFragment
import com.example.matrimony.ui.mainscreen.connectionsscreen.RemoveConnectionListener
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumActivity1
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen.AlbumViewModel
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.SettingsViewModel
import com.example.matrimony.ui.mainscreen.meetingscreen.ScheduleMeetingActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewProfileActivity : AppCompatActivity() {

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val connectionViewModel by viewModels<ConnectionsViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()
    private val albumViewModel by viewModels<AlbumViewModel>()

    lateinit var binding: PageViewProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(TAG, "ViewProfile onCreate")


        val userId = intent.getIntExtra("USER_ID", -1)
        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)


        userProfileViewModel.isCurrentUser = (userId == sharedPref.getInt(CURRENT_USER_ID, -1))

        Log.i(TAG, "ViewProfileAct onCreate")
        Log.i(TAG, "${userProfileViewModel.isCurrentUser}")

        userProfileViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

        binding = DataBindingUtil.setContentView(this, R.layout.page_view_profile)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        userProfileViewModel.currentUserId = userId



        lifecycleScope.launch {
            if (!userProfileViewModel.isCurrentUser) userProfileViewModel.getNameOfUser(userId)
                .observe(this@ViewProfileActivity) {
                    binding.toolbar.title = "$it's Profile"
                }
        }

//        userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
        lifecycleScope.launch {
            userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                .observe(this@ViewProfileActivity) {
                    userProfileViewModel.isUserConnected = it != null && it == "CONNECTED"

                }
//                userProfileViewModel.isConnectionAvailable(userId).value == "CONNECTED"

            Log.i(TAG, "view profile curr user id= ${userProfileViewModel.currentUserId}")
            Log.i(TAG, "isConnected= ${userProfileViewModel.isUserConnected}")
            Log.i(
                TAG,
                "isConnected= ${userProfileViewModel.isConnectionAvailable(userProfileViewModel.userId)}"
            )
            if (userProfileViewModel.isCurrentUser) {
//                binding.extendedFab.visibility = View.GONE
                binding.fabShortlist.visibility = View.GONE
                binding.fabSendConnection.visibility = View.GONE
                binding.fabDial.visibility = View.GONE
                binding.fabMeeting.visibility = View.GONE
            } else {
                setUpCollapsingToolbar()
                initFab()
            }
        }

        binding.collapsingToolbar.contentScrim = null



        if (userProfileViewModel.userId == userProfileViewModel.currentUserId) {
            binding.imgBtnEditAlbum.visibility = View.VISIBLE
            binding.imgBtnEditAlbum.setOnClickListener {
                val intent = Intent(this, AlbumActivity1::class.java)
                startActivity(intent)
            }
        } else binding.imgBtnEditAlbum.visibility = View.GONE


        binding.collapsingToolbar.title = "UserProfile"


        setUpProfileDetailsDisplayPager()
        loadAlbum()
        setUpShortlistFab()
        setUpConnectionsFab()
        setUpDialFab()
        setUpMeetingFab()
    }

    private fun setUpDialFab() {
        binding.fabDial.setOnClickListener {
            lifecycleScope.launch {
                userProfileViewModel.getUserMobile(userProfileViewModel.currentUserId)
                    .observe(this@ViewProfileActivity) {
                        val intent = Intent(Intent.ACTION_DIAL)
                        intent.data = Uri.parse("tel:$it")
                        startActivity(intent)
                    }
            }
        }
    }

    private fun setUpMeetingFab() {
        binding.fabMeeting.setOnClickListener {
            val intent = Intent(this, ScheduleMeetingActivity::class.java)
            intent.putExtra("user_id", userProfileViewModel.currentUserId)
            startActivity(intent)
        }
    }

    var isAllBtnVisible: Boolean? = null

    private fun initFab() {
//        binding.fabShortlist
//        binding.extendedFab.shrink()
//        binding.extendedFab.shrink()
//        binding.extendedFab.shrink()
//        binding.extendedFab.shrink()
        binding.fabShortlist.hide()
        binding.fabSendConnection.hide()
        binding.fabDial.hide()
        binding.fabMeeting.hide()

        isAllBtnVisible = false

        Log.i(TAG, "user connection status=${userProfileViewModel.isUserConnected}")
        if (!userProfileViewModel.isUserConnected) {
            binding.fabDial.visibility = View.GONE
            binding.fabMeeting.visibility = View.GONE
        }

//        binding.extendedFab.setOnClickListener {
            isAllBtnVisible = if (!isAllBtnVisible!!) {
                when (connectionStatus) {
                    ConnectionStatus.CONNECTED -> {
                        binding.fabShortlist.show()
                        binding.fabSendConnection.show()
                        binding.fabDial.show()
                        binding.fabMeeting.show()
//                        binding.extendedFab.extend()
                    }
                    ConnectionStatus.NOT_CONNECTED, ConnectionStatus.REQUESTED,null -> {
//                        binding.fabDial.visibility=View.GONE
//                        binding.fabMeeting.visibility=View.GONE
                        binding.fabShortlist.show()
                        binding.fabSendConnection.show()
//                        binding.extendedFab.extend()
                    }
                }
//                binding.fabShortlist.show()
//                binding.fabSendConnection.show()
//                binding.fabDial.show()
//                binding.fabMeeting.show()
//                binding.extendedFab.extend()
                true
            } else {
//                when(connectionStatus){
//                    ConnectionStatus.CONNECTED->{
//                        binding.fabShortlist.show()
//                        binding.fabSendConnection.show()
//                        binding.fabDial.show()
//                        binding.fabMeeting.show()
//                        binding.extendedFab.extend()
//                    }
//                    ConnectionStatus.NOT_CONNECTED,ConnectionStatus.REQUESTED->{
////                        binding.fabDial.visibility=View.GONE
////                        binding.fabMeeting.visibility=View.GONE
//                        binding.fabShortlist.show()
//                        binding.fabSendConnection.show()
//                        binding.extendedFab.extend()
//                    }
//                }

                binding.fabShortlist.hide()
                binding.fabSendConnection.hide()
                binding.fabDial.hide()
                binding.fabMeeting.hide()
//                binding.extendedFab.shrink()
                false
            }
//        }
    }


    private var isShortlisted = false
    private fun setUpShortlistFab() {
        CoroutineScope(Dispatchers.Main).launch {
            userProfileViewModel.getShortlistedStatus(userProfileViewModel.currentUserId)
                .observe((FragmentComponentManager.findActivity(binding.root.context) as AppCompatActivity)) {

//                    Log.i(TAG, "userId=$userId ShortlistPage Shortlist status Observer")
                    isShortlisted = it
                    if (isShortlisted) {
//                        Log.i(TAG, "$userId saved already")
                        binding.fabShortlist.setImageResource(R.drawable.ic_baseline_favorite_enabled)
//                        binding.imgBtnShortlist.tooltipText = "shortlisted"
                    } else {
                        binding.fabShortlist.setImageResource(R.drawable.ic_baseline_favorite_hollow)
//                        binding.imgBtnShortlist.tooltipText = "shortlist"
                    }
                }
        }

        binding.fabShortlist.setOnClickListener {
            if (isShortlisted) {
                userProfileViewModel.removeShortlist(userProfileViewModel.currentUserId)
                isShortlisted = false
                (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_hollow)
            } else {
                userProfileViewModel.shortlistUser(
                    Shortlists(
                        0, userProfileViewModel.userId, userProfileViewModel.currentUserId
                    )
                )
                isShortlisted = true
                (it as ImageButton).setImageResource(R.drawable.ic_baseline_favorite_enabled)
            }
        }

    }

    private var connectionStatus: ConnectionStatus? = ConnectionStatus.NOT_CONNECTED
    private fun setUpConnectionsFab() {
        CoroutineScope(Dispatchers.Main).launch {
            connectionViewModel.getConnectionDetails(userProfileViewModel.userId,userProfileViewModel.currentUserId)
                .observe(this@ViewProfileActivity) {
                    Log.i(TAG, "Search Adapter CoroutineScope Connection")
                    if (it == null) {
                        binding.fabMeeting.setImageResource(R.drawable.ic_send_connection)
                        return@observe
                    }
                    connectionStatus = when (it.status) {
                        ConnectionStatus.CONNECTED.toString() -> {
//                            binding.fabDial.visibility=View.VISIBLE
//                            binding.fabMeeting.visibility=View.VISIBLE
                            binding.fabSendConnection.setImageResource(R.drawable.ic_remove_connection)
                            ConnectionStatus.CONNECTED
                        }
                        ConnectionStatus.REQUESTED.toString() -> {
                            val value: ConnectionStatus? =
                                if (userProfileViewModel.userId == it.user_id) {
                                    binding.fabSendConnection.setImageResource(R.drawable.ic_connection_sent)
                                    ConnectionStatus.REQUESTED
                                } else {

                                    null
                                }
//                            binding.fabDial.visibility=View.GONE
//                            binding.fabMeeting.visibility=View.GONE
//                            binding.fabSendConnection.setImageResource(R.drawable.ic_connection_sent)
                            //                                    binding.imgBtnSendReq.tooltipText = "requested"
                            value
                        }
                        else -> {
//                            binding.fabDial.visibility = View.GONE
//                            binding.fabMeeting.visibility = View.GONE
                            binding.fabSendConnection.setImageResource(R.drawable.ic_send_connection)
                            //                                    binding.imgBtnSendReq.tooltipText = "not_connected"
                            ConnectionStatus.NOT_CONNECTED
                        }
                    }
                }
        }


        binding.fabSendConnection.setOnClickListener {
            when (connectionStatus) {
                null->{
                    Toast.makeText(this,"Accept the connection request sent by the user",Toast.LENGTH_SHORT).show()
                }
                ConnectionStatus.NOT_CONNECTED -> {
                    connectionStatus = ConnectionStatus.REQUESTED
                    binding.fabSendConnection.setImageResource(R.drawable.ic_connection_sent)
                    userProfileViewModel.sendConnection = true
                }
                ConnectionStatus.REQUESTED -> {
                    connectionStatus = ConnectionStatus.NOT_CONNECTED
                    binding.fabSendConnection.setImageResource(R.drawable.ic_send_connection)
                    userProfileViewModel.sendConnection = false
                }
                ConnectionStatus.CONNECTED -> {
                    val dialog = RemoveConnectionDialogFragment()
                    dialog.removeConnectionListener = RemoveConnectionListener {
                        connectionStatus = ConnectionStatus.NOT_CONNECTED
//                        Toast.makeText(this, "Remove Click", Toast.LENGTH_SHORT).show()
                        binding.fabSendConnection.setImageResource(R.drawable.ic_send_connection)

                        userProfileViewModel.removeConnection(
                            userProfileViewModel.userId, userProfileViewModel.currentUserId
                        )
                    }
                    dialog.show(supportFragmentManager, "remove_connection_dialog")
                }
            }
        }

    }


    private fun loadAlbum() {

        val albumList = mutableListOf<Album>()

        lifecycleScope.launch {
            albumViewModel.getUserAlbum(userProfileViewModel.currentUserId)
                .observe(this@ViewProfileActivity) { album ->
                    Log.i(TAG, "Inside ViewProfile getProfilePic")
                    albumList.clear()
                    album?.forEach { album1 ->
                        if (album1 != null) if (album1.isProfilePic) albumList.add(
                            0,
                            album1
                        )
                        else albumList.add(album1)

                    }
                    val imageList = mutableListOf<Bitmap?>()
//                    albumList.forEach {
//                        imageList.add(it.image)
//                    }
                    lifecycleScope.launch {
                        userProfileViewModel.getConnectionStatus(userProfileViewModel.currentUserId)
                            .observe(this@ViewProfileActivity) { connectionStatus ->
                                lifecycleScope.launch {
                                    settingsViewModel.getPrivacySettings(userProfileViewModel.currentUserId)
                                        .observe(this@ViewProfileActivity) { privacy ->
                                            if (albumList.isNotEmpty()) {

                                                Log.i(TAG,"album")
                                                Log.i(TAG,"current user ${userProfileViewModel.isCurrentUser}")
                                                Log.i(TAG,"privacy profile ${privacy.view_profile_pic}")
                                                Log.i(TAG,"privacy album ${privacy.view_my_album}")
                                                Log.i(TAG,"connection status $connectionStatus")


                                                if (userProfileViewModel.isCurrentUser || privacy.view_profile_pic == "Everyone" || (connectionStatus != null && connectionStatus == "CONNECTED")) {
                                                    if (albumList[0].isProfilePic) {
                                                        imageList.add(albumList[0].image)
                                                        albumList.removeAt(0)
                                                    }
                                                }else{
                                                    if (albumList[0].isProfilePic) {
                                                        albumList.removeAt(0)
                                                    }
                                                }
                                                if (userProfileViewModel.isCurrentUser || privacy.view_my_album == "Everyone" || (connectionStatus != null && connectionStatus == "CONNECTED")) {
                                                    albumList.forEach {
                                                        imageList.add(it.image)
                                                    }
                                                }
                                            }
                                            val imageSwipeAdapter = ImageSwipeAdapter(
                                                this@ViewProfileActivity, imageList
                                            )
                                            binding.viewPagerProfilePics.adapter =
                                                imageSwipeAdapter

                                            TabLayoutMediator(binding.tabLayoutImages,binding.viewPagerProfilePics){
                                                _,_->
                                            }.attach()
                                            Log.i(TAG,"albumListSize : ${imageList.size}")
                                            if(imageList.isNotEmpty() && imageList.size>1){
                                                binding.tabLayoutImages.visibility=View.VISIBLE
                                            }else
                                                binding.tabLayoutImages.visibility=View.GONE

                                            if (imageList.isEmpty()) {
                                                binding.collapsingToolbar.setBackgroundResource(R.drawable.default_profile_pic)
//                                                                            collapsingToolbarLayout.background = resource
                                            }
                                        }
                                }
//                                if ((connectionStatus == null && userProfileViewModel.userId == userProfileViewModel.currentUserId)
//                                    || connectionStatus == "CONNECTED"
//                                ) {
//                                    val imageSwipeAdapter = ImageSwipeAdapter(
//                                        this@ViewProfileActivity, imageList
//                                    )
//                                    binding.viewPagerProfilePics.adapter =
//                                        imageSwipeAdapter
//                                } else if (connectionStatus == null || connectionStatus == "NOT_CONNECTED" || connectionStatus == "REQUESTED") {
//                                    imageList.clear()
//                                    imageList.add(
//                                        BitmapFactory.decodeResource(
//                                            resources, R.drawable.default_profile_pic
//                                        )
//                                    )
//                                    val imageSwipeAdapter = ImageSwipeAdapter(
//                                        this@ViewProfileActivity, imageList
//                                    )
//                                    binding.viewPagerProfilePics.adapter =
//                                        imageSwipeAdapter
//
//                                }
                            }
//                                }

//                                Toast.makeText(
//                                    this@ViewProfileActivity,
//                                    "connectStatus $it",
//                                    Toast.LENGTH_SHORT
//                                ).show()


//                    }

                    }
                }
//            if (albumList.isEmpty()) {
//
//                Glide.with(binding.collapsingToolbar.context).load(R.drawable.default_profile_pic)
//                    .centerCrop().circleCrop().into(object : CustomTarget<Drawable>() {
//                        override fun onLoadCleared(placeholder: Drawable?) {}
//
//                        override fun onResourceReady(
//                            resource: Drawable, transition: Transition<in Drawable>?
//                        ) {
//                            // Set the background image of the CollapsingToolbarLayout
//                            val collapsingToolbarLayout =
//                                findViewById<CollapsingToolbarLayout>(R.id.collapsing_toolbar)
//                            collapsingToolbarLayout.background = resource
//                        }
//                    })
//                binding.collapsingToolbar.setBackgroundColor(
//                    resources.getColor(
//                        android.R.color.transparent, null
//                    )
//                )
//            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        return true
    }

    private fun setUpCollapsingToolbar() {


        binding.appBar.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            val collapsedRange = -appBarLayout!!.totalScrollRange
            if (verticalOffset == collapsedRange) {
                // The AppBarLayout is fully collapsed
//                binding.toolbar.menu.findItem(R.id.menu_edit).isVisible = false
//                binding.extendedFab.hide()
//                isAllBtnVisible = false
//                binding.extendedFab.visibility = View.GONE
//                binding.extendedFab.shrink()
//                binding.fabShortlist.hide()
//                binding.fabSendConnection.hide()
//                binding.fabDial.hide()
//                binding.fabMeeting.hide()
            } else if (verticalOffset == 0) {
                // The AppBarLayout is not fully collapsed
//                binding.toolbar.menu.findItem(R.id.menu_edit).isVisible = true
//                binding.extendedFab.show()
//                binding.extendedFab.visibility = View.VISIBLE
            }
        }

    }


    private fun setUpProfileDetailsDisplayPager() {

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout_profile_details)
        val viewPagerProfileDetails = findViewById<ViewPager2>(R.id.view_pager_profile_details)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, lifecycle).apply {
            add(PersonalInfoFragment())
            add(ReligionInfoFragment())
            add(ProfessionalInfoFragment())
            add(PartnerPreferencesFragment())
        }
        viewPagerProfileDetails.adapter = viewPagerAdapter



        TabLayoutMediator(tabLayout, viewPagerProfileDetails) { tab, position ->
            when (position) {
                0 -> tab.text = "Basic Details"
                1 -> tab.text = "Religion Details"
                2 -> tab.text = "Professional Details"
                3 -> tab.text = "Partner Preferences"
            }
        }.attach()
    }

    override fun onPause() {
        super.onPause()
        if (userProfileViewModel.sendConnection != null && userProfileViewModel.sendConnection!!) {
            userProfileViewModel.addConnection(
                Connections(
                    0, userProfileViewModel.userId, userProfileViewModel.currentUserId, "REQUESTED"
                )
            )
        } else if (userProfileViewModel.sendConnection != null && !userProfileViewModel.sendConnection!!) {
            userProfileViewModel.removeConnection(
                userProfileViewModel.userId, userProfileViewModel.currentUserId
            )
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}