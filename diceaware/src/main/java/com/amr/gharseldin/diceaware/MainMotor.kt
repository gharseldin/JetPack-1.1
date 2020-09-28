package com.amr.gharseldin.diceaware

import android.app.Application
import android.content.*
import android.nfc.Tag
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager

private const val TAG = "MainMotor"
private const val DEFAULT_WORD_COUNT = 6
const val POST_WORDS_TO_LIVE_DATA = "POST_WORDS_TO_LIVE_DATA"

class MainMotor(application: Application) : AndroidViewModel(application) {
    private val _result = MutableLiveData<ViewState>()
    val result: LiveData<ViewState> = _result

    init {
        generatePassphrase(DEFAULT_WORD_COUNT)
        LocalBroadcastManager.getInstance(getApplication()).registerReceiver(
            object :
                BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    (intent?.extras?.getParcelable(POST_WORDS_TO_LIVE_DATA) as ViewState?)?.let {
                        _result.value = it
                    }
                }
            },
            IntentFilter(POST_WORDS_TO_LIVE_DATA)
        );
    }

    fun generatePassphrase() {
        generatePassphrase(
            (result.value as? ViewState.Content)?.wordCount ?: DEFAULT_WORD_COUNT
        )
    }

    fun generatePassphrase(wordCount: Int) {
        _result.value = ViewState.Loading

        val handlerThread = HandlerThread("Generate Words")
        handlerThread.start()
        val looper = handlerThread.looper
        val handler = Handler(looper)

        handler.postDelayed({
            val state = try {
                val randomWords = Repository.generate(
                    getApplication(),
                    wordCount
                )
                ViewState.Content(randomWords.joinToString(" "), wordCount)
            } catch (t: Throwable) {
                ViewState.Error(t)
            }
            // Using a broadcast is simple as in it could be easily received on the main ui
            // But the down side of this is that it will be a lot slower
            /*
            val intent = Intent(POST_WORDS_TO_LIVE_DATA)
            intent.putExtra(POST_WORDS_TO_LIVE_DATA, state)
            LocalBroadcastManager.getInstance(getApplication()).sendBroadcast(intent)
            Log.i(TAG, "BroadCast Sent")
            */
            // Or we can post a runnable on the main thread
            Handler(Looper.getMainLooper()).post{
                _result.value = state
            }
        }, 4000)
    }

}