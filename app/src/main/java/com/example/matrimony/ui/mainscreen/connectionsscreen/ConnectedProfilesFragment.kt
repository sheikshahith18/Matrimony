package com.example.matrimony.ui.mainscreen.connectionsscreen

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.adapters.ConnectedProfilesAdapter
import com.example.matrimony.databinding.FragmentConnectedProfilesBinding
import com.example.matrimony.db.entities.Connections
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.ui.mainscreen.homescreen.profilescreen.ViewProfileActivity
import com.example.matrimony.ui.mainscreen.meetingscreen.ScheduleMeetingActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class ConnectedProfilesFragment : Fragment() {

    lateinit var binding: FragmentConnectedProfilesBinding
    private var fragmentView: View? = null
    private var adapter: ConnectedProfilesAdapter? = null

    private val userProfileViewModel by activityViewModels<UserProfileViewModel>()
    private val connectionsViewModel by activityViewModels<ConnectionsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i(TAG, "ConProf OnCreate")
        Log.i(TAG, "conVM userId :${connectionsViewModel.userId}")
//        Log.i(TAG, "current user id ${userProfileViewModel.userId}")
        if (fragmentView == null) {

            binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_connected_profiles,
                container,
                false
            )
            fragmentView = binding.root

            val sharedPref =
                requireActivity().getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            connectionsViewModel.userId = sharedPref.getInt(CURRENT_USER_ID, -1)

            loadConnectedProfiles()
        }
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        loadConnectedProfiles()
    }


    private val onCallButtonClicked = { userId: Int ->
        lifecycleScope.launch {
            connectionsViewModel.getUserMobile(userId).observe(viewLifecycleOwner) {

                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$it")
                startActivity(intent)
            }
        }
        Unit
    }

    private val onScheduleButtonCLicked: (Int) -> Unit = { userId ->
        val intent = Intent(requireActivity(), ScheduleMeetingActivity::class.java)
        intent.putExtra("user_id", userId)
        startActivity(intent)
    }

    private val onRemoveButtonClicked: (Int, String, Int) -> Unit =
        { userId: Int, buttonText: String, adapterPosition: Int ->
            when (buttonText) {
                "Remove Connection" -> {
                    connectedUserId = userId
                    this.adapterPosition = adapterPosition
                    val dialog = RemoveConnectionDialogFragment()
                    dialog.removeConnectionListener = RemoveConnectionListener {
//                        when (clickedItem) {
//                            "REMOVE_CONNECTION" -> {
//                        Toast.makeText(requireContext(), "Remove Click", Toast.LENGTH_SHORT).show()

                        val viewHolder =
                            binding.rvConnectedProfiles.findViewHolderForAdapterPosition(
                                adapterPosition
                            ) as ConnectedProfilesAdapter.ConnectedProfilesViewHolder
                        viewHolder.btnRemoveConnection.text = "Send Request"
                        viewHolder.btnRemoveConnection.setBackgroundColor(
                            resources.getColor(
                                R.color.teal_200,
                                null
                            )
                        )
                        connectionsViewModel.removeFromConnections.add(connectedUserId)
                        connectedUserId = -1
//                            }
//                        }
                    }


                    val args = Bundle()
                    args.putString("CALLER", this::class.simpleName)
                    dialog.arguments = args
                    dialog.show(
                        childFragmentManager,
                        "remove_connection_dialog"
                    )

                }
                "Send Request" -> {
                    connectedUserId = userId
                    this.adapterPosition = adapterPosition
                    val viewHolder =
                        binding.rvConnectedProfiles.findViewHolderForAdapterPosition(adapterPosition) as ConnectedProfilesAdapter.ConnectedProfilesViewHolder
                    viewHolder.btnRemoveConnection.text = "Request Sent"
                    viewHolder.btnRemoveConnection.setBackgroundColor(
                        resources.getColor(
                            R.color.yellow,
                            null
                        )
                    )
                    viewHolder.btnText.value = "REQUESTED"
//                    adapter?.notifyDataSetChanged()
//                    connectionsViewModel.setConnectionStatus(connectedUserId,"REQUESTED")
                    connectionsViewModel.sendConnectionsTo.add(userId)
//                    adapter?.notifyItemChanged(adapterPosition)
                }
                "Request Sent" -> {
                    connectedUserId = userId
                    val viewHolder =
                        binding.rvConnectedProfiles.findViewHolderForAdapterPosition(adapterPosition) as ConnectedProfilesAdapter.ConnectedProfilesViewHolder
                    viewHolder.btnRemoveConnection.text = "Send Request"
                    viewHolder.btnRemoveConnection.setBackgroundColor(
                        resources.getColor(
                            R.color.teal_200,
                            null
                        )
                    )
                    viewHolder.btnText.value = "NOT_CONNECTED"
//                    adapter?.notifyDataSetChanged()
//                    connectionsViewModel.setConnectionStatus(connectedUserId,"NOT_CONNECTED")
                    connectionsViewModel.sendConnectionsTo.remove(userId)
                }
            }

        }


    //from dialog fragment
    private var connectedUserId = -1
    private var adapterPosition = -1

//    override fun onButtonClick(clickedItem: String) {
//        when (clickedItem) {
//            "REMOVE_CONNECTION" -> {
//                Toast.makeText(requireContext(), "Remove Click", Toast.LENGTH_SHORT).show()
//
//                val viewHolder =
//                    binding.rvConnectedProfiles.findViewHolderForAdapterPosition(adapterPosition) as ConnectedProfilesAdapter.ConnectedProfilesViewHolder
//                viewHolder.btnRemoveConnection.text = "Send Request"
//                viewHolder.btnRemoveConnection.setBackgroundColor(
//                    resources.getColor(
//                        R.color.teal_200,
//                        null
//                    )
//                )
////                val viewHolder =
////                    binding.rvConnectedProfiles.findViewHolderForAdapterPosition(adapterPosition) as ConnectedProfilesAdapter.ConnectedProfilesViewHolder
////                viewHolder.btnText.value="NOT_CONNECTED"
////                adapter?.notifyDataSetChanged()
////                adapter?.notifyItemChanged(adapterPosition)
////                connectionsViewModel.setConnectionStatus(connectedUserId,"NOT_CONNECTED")
////                connectionsViewModel.removeConnection(connectedUserId)
////                connectionsViewModel.setConnectionStatus(connectedUserId,"NOT_CONNECTED")
//                connectionsViewModel.removeFromConnections.add(connectedUserId)
//                connectedUserId = -1
//            }
//        }
//    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "con prof onPause")
        Log.i(TAG, "remove connection ${connectionsViewModel.removeFromConnections.size}")

        var isChanged = false
        connectionsViewModel.removeFromConnections.forEach {
            Log.i(TAG, "remove connection userId $it")
//            connectionsViewModel.setConnectionStatus(it, "NOT_CONNECTED")
            connectionsViewModel.removeConnection(it)
        }
        connectionsViewModel.removeFromConnections.clear()

        connectionsViewModel.sendConnectionsTo.forEach {
            isChanged = true
            connectionsViewModel.addConnection(
                Connections(
                    user_id = connectionsViewModel.userId,
                    connected_user_id = it,
                    status = "REQUESTED"
                )
            )
        }
        connectionsViewModel.sendConnectionsTo.clear()
    }

    private val viewFullProfile: (Int) -> Unit = { userId: Int ->
        val intent = Intent(activity, ViewProfileActivity::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
    }

    private fun loadConnectedProfiles() {
        val connectedProfilesRecyclerView = binding.rvConnectedProfiles
        connectedProfilesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ConnectedProfilesAdapter(
            requireContext(),
            connectionsViewModel,
            onCallButtonClicked,
            onRemoveButtonClicked,
            onScheduleButtonCLicked,
            viewFullProfile
        )
        connectedProfilesRecyclerView.adapter = adapter
        lifecycleScope.launch {
            connectionsViewModel.getConnectedUsers(connectionsViewModel.userId)
                .observe(requireActivity()) {
                    Log.i(TAG, "connect profiles count : ${it.size}")
                    adapter?.setUserList(it)
                    if (it.isEmpty()) {
                        binding.noProfilesMessage.visibility = View.VISIBLE
                        binding.rvConnectedProfiles.visibility = View.GONE
                    } else {
                        binding.noProfilesMessage.visibility = View.GONE
                        binding.rvConnectedProfiles.visibility = View.VISIBLE
                    }
                }
//            connectionsViewModel.getConnectedUserIds(connectionsViewModel.userId)
//                .observe(requireActivity()) {
//                    Log.i(TAG, "connected profiles count :${it.size}")
//                    if (it.isEmpty()) {
//
////                        adapter?.notifyDataSetChanged()
//                        binding.noProfilesMessage.visibility = View.VISIBLE
//                        binding.rvConnectedProfiles.visibility = View.GONE
//                        return@observe
//                    }
//                    binding.noProfilesMessage.visibility = View.GONE
//                    binding.rvConnectedProfiles.visibility = View.VISIBLE
//
//
//                    val usersList = mutableListOf<UserData>()
//
//                    lifecycleScope.launch {
//                        it.forEach { userId ->
//                            val userData = connectionsViewModel.getUserData(userId)
//                            Log.i(TAG, userData.toString())
//                            usersList.add(userData)
//                        }
//                        adapter?.setUserList(usersList)
//                    }
//
//                }
        }
    }


}