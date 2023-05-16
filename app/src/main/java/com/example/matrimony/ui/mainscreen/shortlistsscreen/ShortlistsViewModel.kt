package com.example.matrimony.ui.mainscreen.shortlistsscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.matrimony.db.entities.Shortlists
import com.example.matrimony.db.repository.ShortlistsRepository
import com.example.matrimony.models.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShortlistsViewModel @Inject constructor(private val shortlistsRepository: ShortlistsRepository) :
    ViewModel() {

    var userId = -1
    var gender = ""

    val removeFromShortLists= mutableListOf<Int>()

    fun shortlistUser(shortlist: Shortlists) {
        viewModelScope.launch {
            shortlistsRepository.addShortlist(shortlist)

        }
    }

    fun getShortlistedProfiles(userId: Int): LiveData<List<Int>> {
        return shortlistsRepository.getShortlistedProfiles(userId)
    }

    fun removeShortlist(shortlistedUserId: Int) {
        viewModelScope.launch {
            shortlistsRepository.removeShortlist(this@ShortlistsViewModel.userId, shortlistedUserId)
        }
    }

    suspend fun getShortlistedStatus(shortlistedUserId: Int): LiveData<Boolean> {
        return shortlistsRepository.getShortlistedStatus(this.userId, shortlistedUserId)
    }

    suspend fun getUserData(userId: Int): UserData {
        return shortlistsRepository.getUserData(userId)
    }

}