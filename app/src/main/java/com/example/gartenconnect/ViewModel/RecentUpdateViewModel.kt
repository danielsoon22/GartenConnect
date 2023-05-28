package com.example.gartenconnect.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gartenconnect.model.RecentUpdateRepository
import com.example.gartenconnect.model.Update

class RecentUpdateViewModel: ViewModel(){
    private val _recentUpdate = MutableLiveData<List<Update>>()
    val recentUpdateLiveData: LiveData<List<Update>> = _recentUpdate
    private val recentUpdateRepository: RecentUpdateRepository = RecentUpdateRepository()

    fun getRecentUpdates(limit: Int){
        recentUpdateRepository.getRecentUpdates(limit){
            _recentUpdate.value = it
        }
    }

    fun addUpdate(update: Update){
        recentUpdateRepository.addUpdate(update)
        getRecentUpdates(10)
    }
}