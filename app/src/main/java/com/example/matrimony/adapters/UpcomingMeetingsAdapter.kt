package com.example.matrimony.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ConnectedProfilesViewBinding
import com.example.matrimony.databinding.UpcomingMeetingViewBinding
import com.example.matrimony.db.entities.Meetings
import com.example.matrimony.models.UserData
import com.example.matrimony.ui.mainscreen.meetingscreen.MeetingsViewModel
import dagger.hilt.android.internal.managers.FragmentComponentManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class UpcomingMeetingsAdapter(
    private val context: Context,
    private val meetingsViewModel: MeetingsViewModel,
    private val editMeeting: (Int, Int, Int) -> Unit,
    private val viewFullProfile: (Int) -> Unit
) :
    RecyclerView.Adapter<UpcomingMeetingsAdapter.MeetingRequestsViewHolder>() {

    private var meetingsList = mutableListOf<Meetings>()

    fun setMeetingsList(list: List<Meetings>) {
        Log.i(TAG, "upcomingMeetingsCount ${list.size}")
        this.meetingsList = list.toMutableList()
        notifyDataSetChanged()
    }


    inner class MeetingRequestsViewHolder(private val binding: UpcomingMeetingViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(meeting: Meetings) {
            Log.i(TAG, "meetViewModelUserId, ${meetingsViewModel.userId}")
            Log.i(TAG, "meetSenderUserId, ${meeting.sender_user_id}")
            Log.i(TAG, "meetReceiverUserId, ${meeting.receiver_user_id}")
            val userId =
                if (meetingsViewModel.userId == meeting.receiver_user_id) meeting.sender_user_id else meeting.receiver_user_id

            CoroutineScope(Dispatchers.Main).launch {
                val user = meetingsViewModel.getUserData(userId)
                binding.tvProfileName.text = user.name

                Glide.with(binding.ivProfilePic.context)
                    .load(
                        user.profile_pic
                            ?: ResourcesCompat.getDrawable(
                                context.resources, R.drawable.default_profile_pic,
                                null
                            )
                    )
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(50)))
                    .into(binding.ivProfilePic)

                binding.tvDescription.text = meeting.title
                val date = meeting.date
                val pattern = "dd/MM/yyyy"
                val sdf = SimpleDateFormat(pattern, Locale.getDefault())
                val dateString = sdf.format(date)

                binding.tvWhen.text = "$dateString, ${meeting.time}"
                binding.tvWhere.text = meeting.place
            }
            binding.imgBtnEdit.setOnClickListener {
                editMeeting(meeting.id, meeting.sender_user_id, meeting.receiver_user_id)
            }
            initListeners(userId, meeting.id)
        }

        private fun initListeners(userId: Int, meetingId: Int) {
            binding.btnCancelAppointment.setOnClickListener {
//                Toast.makeText(context, "Cancel Clicked", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "cancel appointment ${meetingsViewModel.userId}, $userId")
                meetingsViewModel.updateMeetingStatusOnId(
                    meetingId,
                    "CANCELLED"
                )
            }
            binding.btnMarkAsComplete.setOnClickListener {
                Log.i(TAG, "complete appointment ${meetingsViewModel.userId}, $userId")
                meetingsViewModel.updateMeetingStatusOnId(
                    meetingId,
                    "COMPLETED"
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingRequestsViewHolder {
        val binding: UpcomingMeetingViewBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.upcoming_meeting_view, parent, false
        )

        return MeetingRequestsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MeetingRequestsViewHolder, position: Int) {
        holder.bind(meetingsList[position])

        holder.itemView.setOnClickListener {
            viewFullProfile(if (meetingsViewModel.userId == meetingsList[position].receiver_user_id) meetingsList[position].sender_user_id else meetingsList[position].receiver_user_id)
        }
    }

    override fun getItemCount(): Int {
        return meetingsList.size
    }
}