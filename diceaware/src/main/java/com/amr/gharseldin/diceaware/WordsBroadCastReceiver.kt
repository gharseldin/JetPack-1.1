package com.amr.gharseldin.diceaware

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

private const val TAG = "WordsBroadCastReceiver"

class WordsBroadCastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(TAG, "Broadcast Received")
        (intent?.extras?.getParcelable(POST_WORDS_TO_LIVE_DATA) as ViewState?)?.let {
            Log.i(TAG, "result = $it")
        }
    }
}