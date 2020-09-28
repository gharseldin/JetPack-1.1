package com.amr.gharseldin.diceaware

import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.amr.gharseldin.diceaware.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val motor : MainMotor by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        registerEventReciever()

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        motor.result.observe(this){ viewState ->
            when(viewState){
                is ViewState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.text.visibility = View.GONE
                }
                is ViewState.Content -> {
                    binding.progress.visibility = View.GONE
                    binding.text.visibility = View.VISIBLE
                    binding.text.text = viewState.passPhrase
                }
                is ViewState.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.text.text = viewState.t.message
                }
            }
        }
    }

    fun registerEventReciever(){
        val eventFilter = IntentFilter()
        eventFilter.addAction(POST_WORDS_TO_LIVE_DATA)
        registerReceiver(WordsBroadCastReceiver(), eventFilter)
    }
}
