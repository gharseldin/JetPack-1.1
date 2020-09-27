package com.amr.gharseldin.looperalternative

import android.util.Log
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.atomic.AtomicBoolean

private const val TAG = "SimpleWorker"
class SimpleWorker: Thread(TAG) {

    private val alive = AtomicBoolean(true)
    private val taskQueue = ConcurrentLinkedDeque<Runnable>()

    init{
        start()
    }

    override fun run() {
        while(alive.get())
            taskQueue.poll()?.run()
        Log.i(TAG, "SimpleWorker Terminated")
    }

    fun execute(task: Runnable):SimpleWorker{
        taskQueue.add(task)
        return this
    }

    fun quit(){
        alive.set(false)
    }
}