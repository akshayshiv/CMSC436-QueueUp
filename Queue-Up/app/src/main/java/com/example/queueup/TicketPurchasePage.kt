package com.example.queueup

import android.os.CountDownTimer

class TicketPurchasePage {
  
  class
  object : CountDownTimer(30000, 1000) {
 
      override fun onTick(millisUntilFinished: Long) {
          mTextField.setText("seconds remaining: " + millisUntilFinished / 1000)
      }
 
      override fun onFinish() {
          mTextField.setText("done!")
      }
  }.start()
  
  private fun setTimerEnabled(isInForeground: Boolean) {
        if (isInForeground) {

            // Update the counter and post the mUpdater Runnable
            with(binding.counter) {
                text = counter.toString()
                postDelayed(updater, DELAY)
            }
        } else {

            // Remove already posted Runnables to stop the ticker's updating
            binding.counter.removeCallbacks(updater)
        }
    }

  
  
}
