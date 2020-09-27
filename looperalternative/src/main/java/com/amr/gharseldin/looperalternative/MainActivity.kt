package com.amr.gharseldin.looperalternative

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //    private val worker = SimpleWorker()
    private val worker = Worker()
    private val handler = Handler(Looper.getMainLooper()) {
        tvMessage.text = it.obj.toString()
        true
    }
    private lateinit var tvMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvMessage = message
        worker.execute { ->
            Thread.sleep(1000)
            val message = Message.obtain()
            message.obj = "Task 1 completed"
            handler.sendMessage(message)
        }.execute {
            Thread.sleep(5000)
            val message = Message.obtain()
            message.obj = "Task 2 completed"
            handler.sendMessage(message)
        }.execute {
            Thread.sleep(1000)
            val message = Message.obtain()
            message.obj = "Task 3 completed"
            handler.sendMessage(message)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        worker.quit()
    }
}
