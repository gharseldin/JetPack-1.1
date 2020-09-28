package com.amr.gharseldin.diceaware

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

sealed class ViewState: Parcelable{
    @Parcelize object Loading: ViewState()
    @Parcelize data class Content(val passPhrase: String, val wordCount: Int): ViewState()
    @Parcelize data class Error(val t: Throwable): ViewState()
}