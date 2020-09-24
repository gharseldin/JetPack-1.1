package com.amr.gharseldin.dicelight

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.amr.gharseldin.dicelight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val motor: MainMotor by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)

        motor.results.observe(this) { viewState ->
            when (viewState) {
                MainViewState.Loading -> {
                    binding.progress.visibility = View.VISIBLE
                    binding.passphrase.text = ""
                }
                is MainViewState.Content -> {
                    binding.progress.visibility = View.GONE
                    binding.passphrase.text = viewState.passPhrase
                }
                is MainViewState.Error -> {
                    binding.progress.visibility = View.GONE
                    binding.passphrase.text = viewState.throwable.localizedMessage
                    Log.e(
                        "Diceware",
                        "Exception generating passphrase",
                        viewState.throwable
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.refresh -> {
                motor.generatePassphrase()
                return true
            }
            R.id.word_count_4, R.id.word_count_5, R.id.word_count_6, R.id.word_count_7,
            R.id.word_count_8, R.id.word_count_9, R.id.word_count_10 -> {
                item.isChecked = !item.isChecked

                motor.generatePassphrase(item.title.toString().toInt())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
