package com.example.queueup

import android.os.CountDownTimer
import android.widget.TextView
class TicketPurchasePage {
    private lateinit var textView : TextView
    private lateinit var binding: MainActivity


    val timer = object : CountDownTimer(30000, 1000) {

      override fun onTick(millisUntilFinished: Long) {
          textView.setText("Seconds remaining to purchase tickets!: " + millisUntilFinished / 1000)
      }

      override fun onFinish() {
          textView.setText("You ran out of time!")
      }
  }.start()

//  private fun setTimerEnabled(isInForeground: Boolean) {
//        if (isInForeground) {
//
//            // Update the counter and post the mUpdater Runnable
//            with(binding.counter) {
//                val text = counter.toString()
//                postDelayed(updater, DELAY)
//            }
//        } else {
//
//            // Remove already posted Runnables to stop the ticker's updating
//            binding.counter.removeCallbacks(updater)
//        }
//    }

  
  
}
