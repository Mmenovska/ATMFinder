package com.gsixacademy.android.atmfinder.adapter

import com.gsixacademy.android.atmfinder.data.AtmsData

sealed class AtmsAdapterClickEvent {
    data class onAtmsAdapterItemClicked ( val atmsData: AtmsData) : AtmsAdapterClickEvent()

}
